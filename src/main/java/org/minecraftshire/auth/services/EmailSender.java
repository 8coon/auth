package org.minecraftshire.auth.services;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;


@Service
public class EmailSender {

    private JavaMailSender sender = new JavaMailSenderImpl();


    public void sendEmailConfirmation(String email, long code, String linkHref) {
        SimpleMailMessage message = new SimpleMailMessage();

        this.setupMessage(message);
        message.setTo(email);
        message.setText("Код подтверждения: " + String.valueOf(code));

        this.sender.send(message);
    }


    private void setupMessage(SimpleMailMessage message) {
        message.setFrom("no-reply@minecraftshire.ru");
    }

}
