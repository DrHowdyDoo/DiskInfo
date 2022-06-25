package com.drhowdydoo.diskinfo;

public class LanguageInfo {

    private String languageCode;
    private String languageTranslator;


    public LanguageInfo(String languageName, String languageTranslator) {
        this.languageCode = languageName;
        this.languageTranslator = languageTranslator;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageTranslator() {
        return languageTranslator;
    }

    public void setLanguageTranslator(String languageTranslator) {
        this.languageTranslator = languageTranslator;
    }
}
