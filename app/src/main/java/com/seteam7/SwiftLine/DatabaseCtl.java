package com.seteam7.SwiftLine;
import android.net.wifi.*;
import android.os.StrictMode;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.*;

import java.io.Console;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.Pipe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class DatabaseCtl implements DatabaseControl{

    private String MAC;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    public DatabaseCtl(String MAC) {
        StrictMode.setThreadPolicy(policy);
        this.MAC = MAC;


        //TESTS
        System.out.println("===============================================");
        System.out.println("TESTS:");
        //System.out.printf("User Test: %s\n", testUser());
        //testReport();

        System.out.println("===============================================");

        setUser();
    }

    private FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }

    private void setUser() {
        if (MAC == null) {
            MAC = "FF:FF:FF:12:34:56";
        }
        CollectionReference users = db().collection("users");
        DocumentReference user = users.document(MAC);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userSnap = task.getResult();
                    if (!userSnap.exists()) {
                        System.out.println("Adding New User");
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("MAC", MAC);
                        newUser.put("LastReportTime", new Timestamp(0, 0));
                        users.document(MAC).set(newUser);
                        try {
                            setUser();
                        } catch (Exception e) {
                            System.out.println("Cannot Access Firebase");
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean sendReport(String team, int waitLength, String mapsID) {
        try {
            CollectionReference locations = db().collection("locations");
            DocumentReference rest = locations.document(mapsID);
            rest.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot userSnap = task.getResult();
                        if (!userSnap.exists()) {
                            System.out.println("Adding New Location");
                            Map<String, Object> newLocation = new HashMap<>();
                            newLocation.put("MapsID", mapsID);
                            newLocation.put("name", "");
                            newLocation.put("location", new GeoPoint(0.0, 0.0));
                            newLocation.put("reportNum", 0);
                            newLocation.put("teamRatio", 0.5);
                            newLocation.put("calcWaitTime", 0);
                            db().collection("locations").document(mapsID).set(newLocation);
                            try {
                                setUser();
                            } catch (Exception e) {
                                System.out.println("Cannot Access Firebase");
                                return;
                            }
                        }
                        Map<String, Object> report = new HashMap<>();
                        report.put("team", team);
                        report.put("time", Timestamp.now());
                        report.put("user", MAC);
                        report.put("waitLength", waitLength);
                        db().collection("locations")
                                .document(mapsID)
                                .collection("reports")
                                .document(MAC + "-"+ LocalDateTime.now().toString()).set(report);
                        db().collection("users").document(MAC).update("LastReportTime", Timestamp.now());
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public float calcTeamRatio(QuerySnapshot reports) {
        int team1 = 0;
        int team2 = 0;
        for (QueryDocumentSnapshot report : reports) {
            if (Objects.equals((String) report.get("team"), "team1"))
                team1++;
            else
                team2++;
        }
        return ((float) team1) / ((float) team2);
    }

    private int reportNum(QuerySnapshot reports) {
        return reports.size();
    }

    private int calcWaitTime(QuerySnapshot reports) {
        int reportCount = reports.size();
        int totalTimeWait = 0;
        for (QueryDocumentSnapshot report : reports) {
            Integer wait = (Integer) report.get("waitLength");
            if (wait == null)
                continue;
            totalTimeWait += wait;
        }
        return totalTimeWait / reportCount;
    }

    private String setUserWrapper() throws SocketException, UnknownHostException {
        setUser();
        return MAC;
    }

    private boolean testUser() {
        try {
            String testMAC = this.setUserWrapper();
            return MAC.equals(testMAC);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void testReport() {
        String testID = "EAuD4dcf210CV1AifWFc3";
        String testTeam = "team1";
        int waitLength = 15;
        sendReport(testTeam, waitLength, testID);
        //for (int i = 0; i < (10^6); i++);
        db().collection("locations").document(testID).collection("reports")
                .document(MAC + "-"+ LocalDateTime.now().toString())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot result = task.getResult();
                    System.out.printf("!!!Report Test: %s, %s", (String) result.get("team"), (int) result.get("waitLength"));
                }
            }
        });
    }

    @Override
    public void setLocations() {
        db().collection("locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().collection("reports").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot result = task.getResult();
                                    float teamRatio = calcTeamRatio(result);
                                    int waitTime = calcWaitTime(result);
                                    int reportNum = reportNum(result);
                                    document.getReference().update("calcWaitTime", waitTime);
                                    document.getReference().update("reportNum", reportNum);
                                    document.getReference().update("teamRatio", teamRatio);
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                        //DO WHAT YOU NEED TO DO
                        Log.d("REPLACE", "Do what you need to do");
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
