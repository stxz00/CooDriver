package com.twk.thymeleafprac.security;

import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log
public class SHAPasswordEncoder implements PasswordEncoder {
    public SHAPasswordEncoder() {}

    @Override
    public String encode(CharSequence raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            final String salt = "ThymeleafPrac";
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            md.update(raw.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%0128x", new BigInteger(1, md.digest()));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
