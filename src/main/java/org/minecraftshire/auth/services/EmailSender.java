package org.minecraftshire.auth.services;


import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.utils.ProcessRunner;
import org.minecraftshire.auth.utils.email.Email;
import org.minecraftshire.auth.utils.email.templates.ConfirmationEmailTemplate;
import org.minecraftshire.auth.utils.email.templates.ConfirmationEmailTemplateParams;
import org.minecraftshire.auth.utils.email.templates.PasswordResetEmailTemplate;
import org.minecraftshire.auth.utils.email.templates.PasswordResetEmailTemplateParams;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;


@Service
public class EmailSender {

    private ConfirmationEmailTemplate confirmationTemplate;
    private PasswordResetEmailTemplate passwordResetTemplate;


    public EmailSender() {
        confirmationTemplate = new ConfirmationEmailTemplate(
                Server.getPath() + "/assets/emails/confirmation.html",
                Charset.forName("utf-8")
        );

        passwordResetTemplate = new PasswordResetEmailTemplate(
                Server.getPath() + "/assets/emails/password-reset.html",
                Charset.forName("utf-8")
        );
    }


    public void sendEmailConfirmation(String to, long code) {
        Email email = confirmationTemplate.getEmail(to, new ConfirmationEmailTemplateParams(String.valueOf(code)));
        EmailSender.sendEmail(email);
    }

    public void sendEmailPasswordReset(String to, long code) {
        Email email = passwordResetTemplate.getEmail(to, new PasswordResetEmailTemplateParams(String.valueOf(code)));
        EmailSender.sendEmail(email);
    }


    public static void sendEmail(Email email) {
        Logger.getLogger().info("sendEmail called");

        String cmd = "echo \"" + email.toString() + "\" | sendmail -t";
        Logger.getLogger().info(cmd);

        ProcessRunner.execAsync(cmd);
    }

}
