package com.drhowdydoo.diskinfo.model;

public class MemInfo {

    private String name;
    private String totalMem;
    private String availMem;
    private String usedMem;
    private String cache;
    private int memBar;

    public MemInfo(String name, String totalMem, String availMem, String usedMem, String cache, int memBar) {
        this.name = name;
        this.totalMem = totalMem;
        this.availMem = availMem;
        this.usedMem = usedMem;
        this.cache = cache;
        this.memBar = memBar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(String totalMem) {
        this.totalMem = totalMem;
    }

    public String getAvailMem() {
        return availMem;
    }

    public void setAvailMem(String availMem) {
        this.availMem = availMem;
    }

    public String getUsedMem() {
        return usedMem;
    }

    public void setUsedMem(String usedMem) {
        this.usedMem = usedMem;
    }

    public int getMemBar() {
        return memBar;
    }

    public void setMemBar(int memBar) {
        this.memBar = memBar;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }


}
