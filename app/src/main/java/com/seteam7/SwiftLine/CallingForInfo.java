package com.seteam7.SwiftLine;

import android.net.Uri;

import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.type.LatLng;

public abstract class CallingForInfo {

    public abstract LatLng getLatLag();

    public abstract String getName();

    public abstract OpeningHours getOpeningHours();

    public abstract String getPhoneNumber();

    public abstract Double getRating();

    public abstract Uri getWebsiteUri();

    public boolean isOpen(boolean status) {
        return status;
    }


}
