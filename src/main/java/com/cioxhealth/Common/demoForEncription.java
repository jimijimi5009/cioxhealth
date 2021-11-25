package com.cioxhealth.Common;

import org.apache.commons.codec.binary.Base64;

public class demoForEncription {

    public static void main(String[] args) {


        System.out.println(encryptedPassword(""));
        System.out.println(decryptedPassword(""));
    }

    public static String encryptedPassword(String passWord){

        byte[] encodedString = new byte[0];
        try {
            encodedString = Base64.encodeBase64(passWord.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(new String(encodedString));
    }
    public static String decryptedPassword(String passWord){

        byte[] decodedString = new byte[0];
        try {
            decodedString = Base64.decodeBase64(passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(new String(decodedString));
    }
}
