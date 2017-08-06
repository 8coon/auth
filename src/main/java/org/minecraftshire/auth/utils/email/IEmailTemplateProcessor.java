package org.minecraftshire.auth.utils.email;


public interface IEmailTemplateProcessor {

    void processParam(Email email, String name, Object value);

}
