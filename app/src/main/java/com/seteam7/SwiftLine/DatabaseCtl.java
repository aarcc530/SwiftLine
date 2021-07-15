package com.seteam7.SwiftLine;
import android.provider.ContactsContract;

import com.google.firebase.firestore.*;

public class DatabaseCtl implements DatabaseControl{

    private FirebaseFirestore db;
    private String MAC;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }


    private boolean checkOrCreate() {

    }

    @Override
    public boolean sendReport(String team, String waitLength, String MAC) {
        return false;
    }

    @Override
    public Location[] getLocations() {
        return new Location[0];
    }
}
