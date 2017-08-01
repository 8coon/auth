package org.minecraftshire.auth.services;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;


@Service
public class EmailSender {

    private Logger log = Logger.getGlobal();


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

        String cmd = "echo \"" + s + "\" | sendmail -t";
        log.info(cmd);

        try {
            Runtime.getRuntime().exec(URLEncoder.encode(cmd,"utf-8"));
        } catch (IOException e) {
            log.severe(e.getLocalizedMessage());
        }
    }

}
