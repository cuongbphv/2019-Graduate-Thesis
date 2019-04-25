package com.graduate.thesis.backend.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author cuongbphv created on 25/04/2019
 */
public class UniqueID {

    static Random rnd = new Random();
    static final String P = "0123456789";

    /**
     * 128 bit UUID
     */
    public static synchronized String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return findAndReplace(uuid, "-", "");
    }

    private static String findAndReplace(String orig, String sub, String rep) {
        StringBuilder out = new StringBuilder();
        int index = 0;
        int oldIndex = index;
        while (index != -1) {
            index = orig.indexOf(sub, index);
            if (index != -1) {
                out.append(orig.substring(oldIndex, index));
                index += sub.length();
                oldIndex = index;
                out.append(rep);
            } else {
                out.append(orig.substring(oldIndex));
            }
        }
        return out.toString();
    }

}
