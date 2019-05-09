package com.graduate.thesis.backend.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author cuongbphv created on 08/05/2019
 */
public class FileUtil {

    private String parentName = null;
    private File dir = null;

    FileUtil(String parentName, File dir) {
        this.parentName = parentName;
        this.dir = dir;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteDirectory2(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            //
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i].getPath());
            }
            //
            dir.delete();
        }
    }

    public static void deleteDirectory(String path) {
        if (path != null) {
            File dir = new File(path);
            if (dir.exists()) {
                //
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        deleteFile(files[i].getPath());
                    } else {
                        deleteDirectory(files[i].getPath());
                    }
                }
                dir.delete();
            }
        }
    }

    public static void copyFile(String in, String out) throws Exception {
        FileChannel ic = new FileInputStream(in).getChannel();
        FileChannel oc = new FileOutputStream(out).getChannel();
        oc.transferFrom(ic, 0, ic.size());
        ic.close();
        oc.close();
    }

    public static void copyFolder(String in, String out) throws Exception {

        File newdir = new File(out);
        if (!newdir.exists()) {
            newdir.mkdir();
        }

        File orgdir = new File(in);

        File[] files = orgdir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {

                copyFile(files[i].getPath(), newdir.getPath() + "\\" + files[i].getName());
            }
        }
    }

    public static void zip(String path) throws Exception {
        int point = path.lastIndexOf(".");
        zip(path, path.substring(0, point));
    }

    public static void zip(String path, String zipFileName) throws Exception {

        File file = new File(path);
        File zipFile = new File(zipFileName + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        if (file.isFile()) {
            addTargetFile(zos, file, file.getName());

        } else {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                int point = zipFile.getName().lastIndexOf(".");
                folderZip(fileList[i].getPath(), fileList[i].getName(), zos, zipFile.getName().substring(0, point));
            }
        }
        zos.close();

    }

    private static void folderZip(String path, String fileName, ZipOutputStream zos, String zipFileName) throws Exception {

        File file = new File(path);

        if (file.isFile()) {
            addTargetFile(zos, file, fileName);
        } else {
            int point = file.getPath().lastIndexOf(zipFileName);
            String zipfolderPath = file.getPath().substring(point + zipFileName.length());
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                folderZip(fileList[i].getPath(), zipfolderPath + "\\" + fileList[i].getName(), zos, zipFileName);
            }
        }
    }

    protected Vector pathNames() {
        String[] list = dir.list();
        File[] files = new File[list.length];
        Vector vec = new Vector();
        for (int i = 0; i < list.length; i++) {

            String pathName = parentName
                    + File.separator + list[i];
            files[i] = new File(pathName);
            if (files[i].isFile()) {
                vec.addElement(files[i].getPath());
            }
        }

        return vec;
    }

    public static void addTargetFile(ZipOutputStream zos, File file, String fileName) throws Exception {
        int EOF = -1;
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        ZipEntry target = new ZipEntry(fileName);
        zos.putNextEntry(target);

        byte buf[] = new byte[1024];
        int count;
        while ((count = bis.read(buf, 0, 1024)) != EOF) {
            zos.write(buf, 0, count);
        }
        bis.close();
        zos.closeEntry();
    }
}
