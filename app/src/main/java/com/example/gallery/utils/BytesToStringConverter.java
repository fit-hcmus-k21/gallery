package com.example.gallery.utils;

import java.text.DecimalFormat;

public class BytesToStringConverter {
    private static final String[] units =
            {"Bytes", "KB", "MB", "GB","TB","PB","EB","ZB","YB","BB","GeB"};
    public static String longToString(long bytes){
        if(bytes < 0){
            return "0";
        }
        int unitIndex = 0;
        double size = bytes;
        while(size >= 1024 && unitIndex < units.length){
            size /= 1024;
            unitIndex++;
        }
        return new DecimalFormat("#.##").format(size)
                + " " + units[unitIndex];
    }
}
