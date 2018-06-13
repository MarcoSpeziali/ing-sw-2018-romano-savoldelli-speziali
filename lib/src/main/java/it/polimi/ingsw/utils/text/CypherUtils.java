package it.polimi.ingsw.utils.text;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CypherUtils {
    private CypherUtils() {}

    public static String decryptString(String toDecrypt, byte[] key, boolean isPrivateKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

        if (isPrivateKey) {
            RSAPrivateKey privateKey = getPrivateKeyFromBytes(key);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        }
        else {
            RSAPublicKey publicKey = getPublicKeyFromBytes(key);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        }
    
        byte[] bytesToDecrypt = Base64.getDecoder().decode(toDecrypt);
        byte[] result = cipher.doFinal(bytesToDecrypt);
        
        return new String(result);
    
        /*StringBuilder stringBuffer = new StringBuilder();
    
        for (byte aResult : result) {
            stringBuffer.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }
    
        return stringBuffer.toString();*/
    }

    public static String encryptString(String toEncrypt, byte[] key, boolean isPrivateKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

        if (isPrivateKey) {
            RSAPrivateKey privateKey = getPrivateKeyFromBytes(key);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        }
        else {
            RSAPublicKey publicKey = getPublicKeyFromBytes(key);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        }

        byte[] bytesToEncrypt = toEncrypt.getBytes();
        byte[] result = cipher.doFinal(bytesToEncrypt);
    
        return new String(Base64.getEncoder().encode(result));
        
        /*StringBuilder stringBuffer = new StringBuilder();
    
        for (byte aResult : result) {
            stringBuffer.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }
        
        return stringBuffer.toString();*/
    }

    /**
     * Constructs a private key (RSA) from the given string
     *
     * @param key PEM Private Key
     * @return RSA Private Key
     * @throws GeneralSecurityException if the given key specification
     *         is inappropriate for this key factory to produce a public key
     */
    private static RSAPrivateKey getPrivateKeyFromBytes(byte[] key) throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * Constructs a public key (RSA) from the given string
     *
     * @param key PEM Public Key
     * @return RSA Public Key
     * @throws GeneralSecurityException if the given key specification
     *         is inappropriate for this key factory to produce a public key
     */
    public static RSAPublicKey getPublicKeyFromBytes(byte[] key) throws GeneralSecurityException {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    
        return (RSAPublicKey) keyFactory.generatePublic(publicSpec);
    }
}
