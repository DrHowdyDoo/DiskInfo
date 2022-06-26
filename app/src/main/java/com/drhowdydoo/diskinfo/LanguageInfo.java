package com.drhowdydoo.diskinfo;

public class LanguageInfo {

    private String languageCode;
    private String languageTranslator;
    private String languageName;
    private boolean showTranslator;


    public LanguageInfo(String languageName, String languageTranslator, String languageName1, boolean showTranslator) {
        this.languageCode = languageName;
        this.languageTranslator = languageTranslator;
        this.languageName = languageName1;
        this.showTranslator = showTranslator;
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

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isShowTranslator() {
        return showTranslator;
    }

    public void setShowTranslator(boolean showTranslator) {
        this.showTranslator = showTranslator;
    }
}
