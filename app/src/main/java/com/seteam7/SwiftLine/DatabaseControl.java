package com.seteam7.SwiftLine;

import java.net.SocketException;
import java.net.UnknownHostException;

public interface DatabaseControl {
    boolean sendReport(String team, int waitLength, String mapsID);
    void setLocations();
}
