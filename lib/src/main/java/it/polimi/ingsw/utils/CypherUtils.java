package it.polimi.ingsw.utils;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class CypherUtils {
    private CypherUtils() {}

    public static String decryptString(String toDecrypt, String key, boolean isPrivateKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");

        if (isPrivateKey) {
            RSAPrivateKey privateKey = getPrivateKeyFromString(key);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        }
        else {
            RSAPublicKey publicKey = getPublicKeyFromString(key);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        }

        return Arrays.toString(cipher.doFinal(toDecrypt.getBytes()));
    }

    public static String encryptString(String toEncrypt, String key, boolean isPrivateKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");

        if (isPrivateKey) {
            RSAPrivateKey privateKey = getPrivateKeyFromString(key);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        }
        else {
            RSAPublicKey publicKey = getPublicKeyFromString(key);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        }

        return Arrays.toString(cipher.doFinal(toEncrypt.getBytes()));
    }

    /**
     * Constructs a private key (RSA) from the given string
     *
     * @param key PEM Private Key
     * @return RSA Private Key
     * @throws GeneralSecurityException if the given key specification
     *         is inappropriate for this key factory to produce a public key
     */
    private static RSAPrivateKey getPrivateKeyFromString(String key) throws GeneralSecurityException {
        String privateKeyPEM = key;

        // Remove the first and last lines
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");

        // Base64 decode data
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    /**
     * Constructs a public key (RSA) from the given string
     *
     * @param key PEM Public Key
     * @return RSA Public Key
     * @throws GeneralSecurityException if the given key specification
     *         is inappropriate for this key factory to produce a public key
     */
    public static RSAPublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        String publicKeyPEM = key;

        // Remove the first and last lines
        // publicKeyPEM = publicKeyPEM.replace("ssh-rsa ", "");
        // publicKeyPEM = publicKeyPEM.replaceAll("\\s[^\\s]*?@.*$", "");

        // Base64 decode data
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
    }
}
