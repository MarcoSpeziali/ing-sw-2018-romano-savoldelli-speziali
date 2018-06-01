package it.polimi.ingsw.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class HashUtils {
    private HashUtils() {}

    public static String sha1(String original) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            return Arrays.toString(digest.digest(
                    original.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
