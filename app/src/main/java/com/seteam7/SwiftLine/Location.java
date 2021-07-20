package com.seteam7.SwiftLine;

import com.google.firebase.firestore.GeoPoint;

public interface Location {
    public String getMapsID();
    public String getLocName();
    public double getTeamRatio();
    public int getWaitTime();
}
