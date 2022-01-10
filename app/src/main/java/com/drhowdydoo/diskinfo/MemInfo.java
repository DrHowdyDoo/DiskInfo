package com.drhowdydoo.diskinfo;

public class MemInfo {

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

    private String name, totalMem, availMem, usedMem;
    private int memBar;

    public MemInfo(String name, String totalMem, String availMem, String usedMem, int memBar) {
        this.name = name;
        this.totalMem = totalMem;
        this.availMem = availMem;
        this.usedMem = usedMem;
        this.memBar = memBar;
    }


}
