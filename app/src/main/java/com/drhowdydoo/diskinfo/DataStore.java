package com.drhowdydoo.diskinfo;

public class DataStore {

    private String mount_name;
    private String fileSystem;

    private String totalSize;
    private String usedSize;
    private String freeSize;
    private String blockSize;

    private boolean access_type;

    private long total;
    private long unused;
    private long used;
    private long blockSpace;

    private int progress;

    public DataStore(String mount_name, String fileSystem, String totalSize, String usedSize, String freeSize, String blockSize, boolean access_type, long total, long unused, long used, long blockSpace, int progress) {
        this.mount_name = mount_name;
        this.fileSystem = fileSystem;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.freeSize = freeSize;
        this.blockSize = blockSize;
        this.access_type = access_type;
        this.total = total;
        this.unused = unused;
        this.used = used;
        this.blockSpace = blockSpace;
        this.progress = progress;
    }

    public DataStore(DataStore obj) {
        this.mount_name = obj.mount_name;
        this.fileSystem = obj.fileSystem;
        this.totalSize = obj.totalSize;
        this.usedSize = obj.usedSize;
        this.freeSize = obj.freeSize;
        this.blockSize = obj.blockSize;
        this.access_type = obj.access_type;
        this.total = obj.total;
        this.unused = obj.unused;
        this.used = obj.used;
        this.blockSpace = obj.blockSpace;
        this.progress = obj.progress;
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

    public long getBlockSpace() {
        return blockSpace;
    }

    public void setBlockSpace(long blockSpace) {
        this.blockSpace = blockSpace;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(String usedSize) {
        this.usedSize = usedSize;
    }

    public String getFreeSize() {
        return freeSize;
    }

    public void setFreeSize(String freeSize) {
        this.freeSize = freeSize;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
