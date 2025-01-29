package com.chatapp.utils;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import java.security.SecureRandom;
import java.util.Base64;

public class HashPassword {

    public static String hashPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(2) // Number of iterations
                .withMemoryPowOfTwo(16) // Memory usage (64MB)
                .withParallelism(4); // Number of threads

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] result = new byte[32]; // Output hash length
        generator.generateBytes(password.toCharArray(), result);

        // Encode the salt and the hashed password to Base64 and concatenate them with a colon
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encodedHash = Base64.getEncoder().encodeToString(result);

        return encodedSalt + ":" + encodedHash;
    }

    public static boolean verifyPassword(String password, String storedHash) {
        // Split the stored hash into salt and hashed password
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid stored hash format");
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        // Recreate the Argon2 hash with the same salt
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(2) // Number of iterations
                .withMemoryPowOfTwo(16) // Memory usage (64MB)
                .withParallelism(4); // Number of threads

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] actualHash = new byte[32]; // Output hash length
        generator.generateBytes(password.toCharArray(), actualHash);

        // Compare the newly generated hash with the stored hash
        return slowEquals(expectedHash, actualHash);
    }

    // Securely compare two byte arrays to prevent timing attacks
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

}