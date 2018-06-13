package it.polimi.ingsw.utils.text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {
    private HashUtils() {}

    public static String sha1(String original) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] result = messageDigest.digest(original.getBytes());

            StringBuilder stringBuffer = new StringBuilder();

            for (byte aResult : result) {
                stringBuffer.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
