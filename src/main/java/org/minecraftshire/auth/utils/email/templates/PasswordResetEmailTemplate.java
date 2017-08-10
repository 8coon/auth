package org.minecraftshire.auth.utils.email.templates;


import java.nio.charset.Charset;


public class PasswordResetEmailTemplate extends MinecraftshireEmailTemplate {

    public PasswordResetEmailTemplate(String fileName, Charset charset) {
        super(fileName, charset);
    }

    @Override
    public String getSubject() {
        return "Change password for minecraftshire.ru";
    }

}
