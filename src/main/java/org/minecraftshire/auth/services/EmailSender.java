package org.minecraftshire.auth.services;


import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Service
public class EmailSender {

    private Logger log = Logger.getLogger();


    public void sendEmailConfirmation(String email, long code, String linkHref) {
        sendEmail(email, "Подтверждение адреса электронной почты", String.valueOf(code));
    }


    private void sendEmail(String to, String subject, String text) {
        String s = "" +
            "From: no-reply@minecraftshire.ru\n" +
            "To: " + to + "\n" +
            "MIME-Version: 1.0\n" +
            "Content-Type: text/html; charset=\\\"utf-8\\\"\n" +
            "Subject: " + subject + "\n" +
            "\n" + text.replace("\n", "<br>");

        String letter = "";

        try {
            letter = URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e);

            return;
        }

        final String cmd = "echo \"" + letter + "\" | sendmail -t";
        ProcessRunner.execAsync(cmd);

        log.info(cmd);
    }

}
