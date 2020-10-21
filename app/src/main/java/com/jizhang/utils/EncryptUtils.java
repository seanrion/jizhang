package com.jizhang.utils;

import android.util.Base64;

import java.security.MessageDigest;

public class EncryptUtils {
    private EncryptUtils(){

    }
    public static String Encode(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = Base64.encodeToString(md.digest(origin.getBytes()),Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

}
