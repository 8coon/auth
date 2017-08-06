package org.minecraftshire.auth.services;


import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Service
public class EmailSender {


    public void sendEmailConfirmation(String email, long code, String linkHref) {
        sendEmail(email, "Подтверждение адреса электронной почты", String.valueOf(code));
    }


    private void sendEmail(String to, String subject, String text) {
        Logger.getLogger().info("sendEmail called");

        String s = "" +
            "From: no-reply@minecraftshire.ru\n" +
            "To: " + to + "\n" +
            "MIME-Version: 1.0\n" +
            "Content-Type: text/html; charset=\\\"utf-8\\\"\n" +
            "Subject: " + subject + "\n" +
            "\n" + text.replace("\n", "<br>");

        String cmd = "echo \"" + s + "\" | sendmail -t";
        Logger.getLogger().info(cmd);

        ProcessRunner.execAsync(cmd);
    }

}
