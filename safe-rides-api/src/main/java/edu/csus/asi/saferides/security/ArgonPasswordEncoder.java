package edu.csus.asi.saferides.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ArgonPasswordEncoder implements PasswordEncoder {

    private static final int SALTLEN = 32;
    private static final int HASHLEN = 64;

    @Override
    public String encode(CharSequence charSequence) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, SALTLEN, HASHLEN);
        // Hash password
        String hash = argon2.hash(2, 65536, 1, charSequence.toString());
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
        return hash;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.verify(s, charSequence.toString());
    }
}
