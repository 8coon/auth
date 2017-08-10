package org.minecraftshire.auth.utils.email.templates;


import org.minecraftshire.auth.utils.email.EmailTemplateParam;


public class PasswordResetEmailTemplateParams {

    private String validationCode;

    public PasswordResetEmailTemplateParams(String validationCode) {
        this.validationCode = validationCode;
    }

    @EmailTemplateParam
    public String getVALIDATION_CODE() {
        return this.validationCode;
    }

}
