package edu.csus.asi.saferides.utility;

import edu.csus.asi.saferides.security.model.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NOTE: this class is being documented in feature/app-active-inactive
 */
public class Util {

    public static String formatAddress(String line1, String line2, String city) {
        if (line2 == null || line2.length() == 0) {
            return String.format("%s, %s, %s", line1, city, "CA");
        } else {
            return String.format("%s %s, %s, %s", line1, line2, city, "CA");
        }
    }

    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }

    public static boolean isPasswordValid(String password) {
        return (password.length() > 8);
    }

}
