package edu.csus.asi.saferides.utility;

public class Util {

    public static String formatAddress(String line1, String line2, String city, String zip) {
        if (line2 == null || line2.length() == 0) {
            return String.format("%s, %s, %s %s", line1, city, "CA", zip);
        } else {
            return String.format("%s %s, %s, %s %s", line1, line2, city, "CA", zip);
        }
    }
}
