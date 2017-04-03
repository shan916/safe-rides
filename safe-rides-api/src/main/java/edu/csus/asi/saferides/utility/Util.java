package edu.csus.asi.saferides.utility;

public class Util {

    public static String formatAddress(String line1, String line2, String city) {
        if (line2 == null || line2.length() == 0) {
            return String.format("%s, %s, %s", line1, city, "CA");
        } else {
            return String.format("%s %s, %s, %s", line1, line2, city, "CA");
        }
    }
}
