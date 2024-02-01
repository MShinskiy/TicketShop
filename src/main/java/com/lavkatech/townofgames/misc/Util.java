package com.lavkatech.townofgames.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Util {

    private static final Logger log = LogManager.getLogger();

    public static String renderString(String toRender, String... vars) {
        for(int i = 1; i <=3; i++)
            toRender = toRender.replace("{" + i + "}", vars[i-1]);
        return toRender;
    }

    //AES256CBC Message decryption
    public static String decrypt(String encrypted, String initVector, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            log.error(ex);
        }
        //Null on error
        return null;
    }

    public static String encrypt(String input, String key,
                                 String initVector) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.encodeBase64String(cipherText);
    }
}
