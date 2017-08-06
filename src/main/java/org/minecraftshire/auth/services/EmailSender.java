package org.minecraftshire.auth.services;


import org.minecraftshire.auth.MinecraftshireAuthApplication;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.email.Email;
import org.minecraftshire.auth.utils.email.templates.ConfirmationEmailTemplate;
import org.minecraftshire.auth.utils.email.templates.ConfirmationEmailTemplateParams;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;


@Service
public class EmailSender {

    private ConfirmationEmailTemplate confirmationTemplate;


    public EmailSender() {
        confirmationTemplate = new ConfirmationEmailTemplate(
                MinecraftshireAuthApplication.getPath() + "/assets/emails/confirmation.html",
                Charset.forName("utf-8")
        );
    }


    public void sendEmailConfirmation(String to, long code) {
        Email email = confirmationTemplate.getEmail(to, new ConfirmationEmailTemplateParams(String.valueOf(code)));
        EmailSender.sendEmail(email);
    }


    public static void sendEmail(Email email) {
        Logger.getLogger().info("sendEmail called");

        String cmd = "echo \"" + email.toString() + "\" | sendmail -t";
        Logger.getLogger().info(cmd);

        ProcessRunner.execAsync(cmd);
    }

}
