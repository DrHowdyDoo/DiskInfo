package com.drhowdydoo.diskinfo;

public class Util {

    public static String FormatBytes(long bytes) {

        if (bytes < 1024) return bytes + " Bytes";
        else if (bytes / 1024 < 1024) return (bytes / 1024) + " KB";
        else if (bytes / (1024 * 1024) < 1024) return (bytes / (1024 * 1024)) + " MB";
        else return (bytes / (1024 * 1024 * 1024)) + " GB";
    }


    public static int getUsedSpace(long total, long used) {

        return used == 0 ? 0 : (int) (100 / (total / used));
    }

}
