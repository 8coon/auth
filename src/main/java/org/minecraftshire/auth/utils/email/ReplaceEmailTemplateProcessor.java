package org.minecraftshire.auth.utils.email;


public class ReplaceEmailTemplateProcessor implements IEmailTemplateProcessor {

    @Override
    public void processParam(Email email, String name, Object value) {
        email.setBody(email.getBody().replace("$" + name.toUpperCase() + "$", value.toString()));
    }

}
