package org.minecraftshire.auth.services;


import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;


@Service
public class PasswordEncoder {

    private SecureRandom random = new SecureRandom();


    public int generateSalt() {
        return this.random.nextInt();
    }

    public String makeSalty(String password, int salt) {
        Random random = new Random(salt);
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger().error(e);
            System.exit(-1);
        }

        byte[] intermediate = null;

        try {
            intermediate = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger().error( e);
            System.exit(-1);
        }

        int upper = 100 + random.nextInt(500);

        for (int i = 0; i < upper; i++) {
            intermediate = md.digest(intermediate);
        }

        return Base64.getEncoder().encodeToString(intermediate);
    }


}
