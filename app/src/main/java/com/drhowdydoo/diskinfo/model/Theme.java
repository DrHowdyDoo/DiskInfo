package com.drhowdydoo.diskinfo.model;

public class Theme {

    private int theme;
    private int amoledVersion;
    private int id;

    public Theme(int theme, int amoledVersion, int id) {
        this.theme = theme;
        this.amoledVersion = amoledVersion;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getAmoledVersion() {
        return amoledVersion;
    }

    public void setAmoledVersion(int amoledVersion) {
        this.amoledVersion = amoledVersion;
    }

}
