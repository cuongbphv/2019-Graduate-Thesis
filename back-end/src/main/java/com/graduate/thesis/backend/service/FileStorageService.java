package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.config.FileStorageProperties;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.util.APIStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Huy Pham
 */


@Service
public class FileStorageService {

    private static final int BUFFER_SIZE = 100 * 1024;

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {

        }
    }

    public String storeFile(MultipartFile file, String fileName) {
        // Normalize file name
        try {
            // Check if the file's name contains invalid characters
            if(!fileName.contains("..")) {
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            return this.fileStorageLocation.toString() + "\\" + fileName;

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ApplicationException(APIStatus.ERR_CANT_STORE);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            }

            throw new ApplicationException(APIStatus.ERR_FILE_NOT_FOUND);

        } catch (MalformedURLException ex) {
            throw new ApplicationException(APIStatus.ERR_FILE_NOT_FOUND);
        }
    }

    /**
     * Append new input stream into specific file
     *
     * @param in
     * @param destFile
     */
    public void appendFile(InputStream in, File destFile) {
        OutputStream out = null;
        try {
            if (destFile.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
            }
            in = new BufferedInputStream(in, BUFFER_SIZE);

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
