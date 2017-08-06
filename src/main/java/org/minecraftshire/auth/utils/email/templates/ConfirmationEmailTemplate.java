package org.minecraftshire.auth.utils.email.templates;


import java.nio.charset.Charset;


public class ConfirmationEmailTemplate extends MinecraftshireEmailTemplate {

    public ConfirmationEmailTemplate(String fileName, Charset charset) {
        super(fileName, charset);
    }

    @Override
    public String getSubject() {
        return "Регистрация на minecraftshire.ru";
    }

}
