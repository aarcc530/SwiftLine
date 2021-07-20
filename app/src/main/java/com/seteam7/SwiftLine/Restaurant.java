package com.seteam7.SwiftLine;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

public class Restaurant implements Location{
    String mapsID;
    String locName;
    double teamRatio;
    int waitTime;
    int totalReports;

    public Restaurant(String mapsID, String locName, double teamRatio, int totalReports, int waitTime) {
        this.mapsID = mapsID;
        this.locName = locName;
        this.teamRatio = teamRatio;
        this.waitTime = waitTime;
        this.totalReports = totalReports;
    }

    public void setTeamRatio(float teamRatio) {
        this.teamRatio = teamRatio;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public String getMapsID() {
        return mapsID;
    }

    @Override
    public String getLocName() {
        return locName;
    }

    @Override
    public double getTeamRatio() {
        return teamRatio;
    }

    @Override
    public int getWaitTime() {
        return waitTime;
    }
}
