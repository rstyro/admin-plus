package com.lrs.common.utils;

public class PathsUtils {
    public static String getAbsolutePath(String path){
        return System.getProperty("user.dir")+"/"+path;
    }
}
