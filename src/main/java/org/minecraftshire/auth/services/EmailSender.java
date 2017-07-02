package org.minecraftshire.auth.services;


import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class EmailSender {


    public void sendEmailConfirmation(String email, long code, String linkHref) {
        sentEmail(email, "Подтверждение адреса электронной почты", String.valueOf(code));
    }


    private void sentEmail(String to, String subject, String text) {
        String cmd = "echo \"" + text.replace("\"", "\\\"") + "\" | mail -s \"" + subject +
                "\" -a \"From: no-reply@minecraftshire.ru\" " + to;

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
