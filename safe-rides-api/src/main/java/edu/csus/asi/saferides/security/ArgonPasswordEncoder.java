package edu.csus.asi.saferides.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class provides a wrapper to allow salting/hashing as well as verifying
 * passwords against previously salted/hashed passwords
 * https://github.com/phxql/argon2-jvm
 */
@Component
public class ArgonPasswordEncoder implements PasswordEncoder {

    // static variables to define size of salt and hash
    private static final int SALTLEN = 32;
    private static final int HASHLEN = 64;

    @Override
    public String encode(CharSequence charSequence) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, SALTLEN, HASHLEN);
        // Hash password using default values
        /**
         * String hash(int iterations, int memory, int parallelism, char[] password);
         * Hashes a password.
         * @param iterations  Number of iterations
         * @param memory      Sets memory usage to x kibibytes
         * @param parallelism Number of threads and compute lanes
         * @param password    Password to hash
         * @param charset     Charset of the password
         * @return Hashed password.
         */
        String hash = argon2.hash(2, 65536, 1, charSequence.toString());
        return hash;
    }

    /**
     * Matches method allows checking a password against a previously hashed password
     *
     * @param charSequence the password from input
     * @param s            the password from storage
     * @return boolean if the two parameters match
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.verify(s, charSequence.toString());
    }
}
