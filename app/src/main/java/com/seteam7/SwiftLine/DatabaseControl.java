package com.seteam7.SwiftLine;

public interface DatabaseControl {
    boolean sendReport(String team, String waitLength, String MAC);
    Location[] getLocations();
}
