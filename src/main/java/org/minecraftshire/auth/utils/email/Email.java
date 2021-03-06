package org.minecraftshire.auth.utils.email;


import java.nio.charset.Charset;
import java.util.Base64;

public class Email {

    private String body = "";
    private String subject = "";
    private String from = "";
    private String to = "";

    public Email() {
    }

    public Email(String body, String subject, String from, String to) {
        this.body = body;
        this.subject = subject;
        this.from = from;
        this.to = to;
    }


    public String toString() {
        return "" +
                "From: " + this.getFrom() + "\n" +
                "To: " + this.getTo() + "\n" +
                "MIME-Version: 1.0\n" +
                "Content-Type: text/html; charset=\\\"utf-8\\\"\n" +
                "Content-Transfer-Encoding: base64\n" +
                "Subject: " + this.getSubject() + "\n" +
                "\n" + Base64.getEncoder().encodeToString(this.getBody().getBytes(Charset.forName("utf-8")));
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
