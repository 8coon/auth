package org.minecraftshire.auth.utils.email.templates;


import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.utils.email.EmailFileTemplate;

import java.nio.charset.Charset;


public abstract class MinecraftshireEmailTemplate extends EmailFileTemplate {

    public MinecraftshireEmailTemplate(String fileName, Charset charset) {
        super(fileName, charset);
    }

    @Override
    public String getFrom() {
        return Server.getEnv().getProperty("minecraftshire.fromEmail");
    }

}
