package com.twk.thymeleafprac.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Util {
    public static String getSHAPassword(CharSequence s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            final String salt = "HEALTHNM";
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            md.update(s.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%0128x", new BigInteger(1, md.digest()));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRandomStringUtils(int length) {
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return  temp.toString();
    }
}
