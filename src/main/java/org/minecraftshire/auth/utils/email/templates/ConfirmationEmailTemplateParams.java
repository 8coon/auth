package org.minecraftshire.auth.utils.email.templates;


import org.minecraftshire.auth.utils.email.EmailTemplateParam;


public class ConfirmationEmailTemplateParams {

    private String validationCode;

    public ConfirmationEmailTemplateParams(String validationCode) {
        this.validationCode = validationCode;
    }

    @EmailTemplateParam
    public String getVALIDATION_CODE() {
        return this.validationCode;
    }

}
