package com.seteam7.SwiftLine;
import android.app.Activity;
import android.net.wifi.*;
import android.os.StrictMode;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
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

public class DatabaseCtl{

    private static String MAC;
    public static PlacesClient placesClient;

    public static void setupDatabaseCtl(String MACaddr, Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MAC = MACaddr;

        Places.initialize(context, "AIzaSyCtq3cvENlmH-euDbz4VrwYiFUL8VkTw04");
        placesClient = Places.createClient(context);
        setUser();
    }

    private static FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }

    private static void setUser() {
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


    public static boolean sendReport(String team, int waitLength, String mapsID) {
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

    public static float calcTeamRatio(QuerySnapshot reports) {
        int team1 = 0;
        int team2 = 0;
        for (QueryDocumentSnapshot report : reports) {
            if (Objects.equals((String) report.get("team"), "team1"))
                team1++;
            else
                team2++;
        }
        return ((float) team1) / ((float) team2 + team1);
    }

    private static int reportNum(QuerySnapshot reports) {
        return reports.size();
    }

    private static int calcWaitTime(QuerySnapshot reports) {
        int reportCount = reports.size();
        int totalTimeWait = 0;
        for (QueryDocumentSnapshot report : reports) {
            long wait = (long) report.get("waitLength");
            totalTimeWait += wait;
        }
        if (reportCount == 0) {
            return 0;
        }
        return totalTimeWait / reportCount;
    }

    private static String setUserWrapper() throws SocketException, UnknownHostException {
        setUser();
        return MAC;
    }

    private static boolean testUser() {
        try {
            String testMAC = setUserWrapper();
            return MAC.equals(testMAC);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void sendTestReport() {
        String[] testIDs = {"ChIJh7B58RRQa4gRC-5C44pbDh8", "ChIJmXgHhsGpbIgRCjim2_73gak", "ChIJma3kJaNQa4gRCpb8RKqpIR8"};
        int waitLength = 15;
        for (String testID : testIDs) {
            sendReport("team1", waitLength, testID);
            sendReport("team2", waitLength, testID);
        }

    }


    public static void setLocations(MapsActivity map, GoogleMap nmap) {
        db().collection("locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        doc.getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent( DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
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
                                Location loc = new Restaurant((String) document.get("mapsID"), (String) document.get("name"),
                                        (double) document.get("teamRatio"), ((Long) document.get("reportNum")).intValue(),
                                        ((Long) document.get("calcWaitTime")).intValue());
                                map.placeLocation(loc, nmap);
                            }
                        }
                        );
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    public static BitmapDescriptor getCorrectIconMap(double teamRatio, Activity activity) {
        int roundedPercent = ((Long) Math.round(teamRatio * 10)).intValue();
        BitmapDescriptor toReturn;
        switch (roundedPercent) {
            case 0:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_0", "drawable", activity.getPackageName()));
                break;
            case 1:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_10", "drawable", activity.getPackageName()));
                break;
            case 2:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_20", "drawable", activity.getPackageName()));
                break;
            case 3:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_30", "drawable", activity.getPackageName()));
                break;
            case 4:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_40", "drawable", activity.getPackageName()));
                break;
            case 6:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_60", "drawable", activity.getPackageName()));
                break;
            case 7:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_70", "drawable", activity.getPackageName()));
                break;
            case 8:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_80", "drawable", activity.getPackageName()));
                break;
            case 9:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_90", "drawable", activity.getPackageName()));
                break;
            case 10:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_100", "drawable", activity.getPackageName()));
                break;
            default:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("rest_50", "drawable", activity.getPackageName()));
        }
        return toReturn;
    }
    public static BitmapDescriptor getCorrectIconReport(double teamRatio, Activity activity) {
        int roundedPercent = ((Long) Math.round(teamRatio * 10)).intValue();
        BitmapDescriptor toReturn;
        switch (roundedPercent) {
            case 0:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_0", "drawable", activity.getPackageName()));
                break;
            case 1:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_10", "drawable", activity.getPackageName()));
                break;
            case 2:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_20", "drawable", activity.getPackageName()));
                break;
            case 3:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_30", "drawable", activity.getPackageName()));
                break;
            case 4:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_40", "drawable", activity.getPackageName()));
                break;
            case 6:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_60", "drawable", activity.getPackageName()));
                break;
            case 7:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_70", "drawable", activity.getPackageName()));
                break;
            case 8:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_80", "drawable", activity.getPackageName()));
                break;
            case 9:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_90", "drawable", activity.getPackageName()));
                break;
            case 10:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_100", "drawable", activity.getPackageName()));
                break;
            default:
                toReturn = BitmapDescriptorFactory.fromResource(activity.getResources().getIdentifier("team_50", "drawable", activity.getPackageName()));
        }
        return toReturn;
    }
}
