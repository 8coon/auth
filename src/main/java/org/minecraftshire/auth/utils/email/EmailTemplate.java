package org.minecraftshire.auth.utils.email;


import org.minecraftshire.auth.utils.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public abstract class EmailTemplate {

    protected abstract Email nextEmail();
    protected abstract IEmailTemplateProcessor getTemplateProcessor();


    public Email getEmail(String to, Object params) {
        Email email = this.nextEmail();
        email.setTo(to);

        for (Method method: params.getClass().getMethods()) {
            if (method.isAnnotationPresent(EmailTemplateParam.class)) {
                String paramName = method.getName().replace("get", "").toLowerCase();
                Object paramValue;

                try {
                    paramValue = method.invoke(params);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Logger.getLogger().severe(e);
                    paramValue = "null";
                }

                this.getTemplateProcessor().processParam(email, paramName, paramValue);
            }
        }

        return email;
    }

}
