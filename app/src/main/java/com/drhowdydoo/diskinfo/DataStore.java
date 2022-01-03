package com.drhowdydoo.diskinfo;

public class DataStore {

    private String mount_name,
            fileSystem;

    private boolean access_type;

    private long total,
            unused,
            used;

    public DataStore(String mount_name, String fileSystem, boolean access_type, long total, long unused, long used) {
        this.mount_name = mount_name;
        this.fileSystem = fileSystem;
        this.access_type = access_type;
        this.total = total;
        this.unused = unused;
        this.used = used;
    }

    public String getMount_name() {
        return mount_name;
    }

    public void setMount_name(String mount_name) {
        this.mount_name = mount_name;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public long getUnused() {
        return unused;
    }

    public void setUnused(long unused) {
        this.unused = unused;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public boolean isAccess_type() {
        return access_type;
    }

    public void setAccess_type(boolean access_type) {
        this.access_type = access_type;
    }
}
