package org.minecraftshire.auth.utils.email;


import org.minecraftshire.auth.utils.logging.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class EmailFileTemplate extends EmailTemplate {

    private ReplaceEmailTemplateProcessor processor = new ReplaceEmailTemplateProcessor();
    private String bodyTemplate = "";


    public EmailFileTemplate(String fileName, Charset charset) {
        try {
            this.bodyTemplate = new String(Files.readAllBytes(Paths.get(fileName)), charset);
        } catch (IOException e) {
            Logger.getLogger().severe(e);
        }
    }


    @Override
    protected Email nextEmail() {
        return new Email(bodyTemplate, this.getSubject(), this.getFrom(), "");
    }

    @Override
    protected IEmailTemplateProcessor getTemplateProcessor() {
        return this.processor;
    }


    public abstract String getSubject();
    public abstract String getFrom();

}
