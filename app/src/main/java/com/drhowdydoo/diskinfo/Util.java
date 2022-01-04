package com.drhowdydoo.diskinfo;

import java.text.DecimalFormat;

public class Util {

    public static String FormatBytes(long bytes) {

        double bytesInDouble = (double) bytes;
        DecimalFormat df = new DecimalFormat("#.#");

        if (bytes < 1024) return bytes + " Bytes";
        else if (bytes / 1024 < 1024) return df.format((bytesInDouble / 1024.0)) + " KB";
        else if (bytes / (1024 * 1024) < 1024)
            return df.format((bytesInDouble / (1024.0 * 1024.0))) + " MB";
        else return df.format((bytesInDouble / (1024.0 * 1024.0 * 1024.0))) + " GB";
    }


    public static int getUsedSpace(long total, long used) {

        return used == 0 ? 0 : (int) (100 / (total / used));
    }

}
