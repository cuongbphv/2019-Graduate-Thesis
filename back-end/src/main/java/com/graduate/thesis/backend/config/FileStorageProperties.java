package com.graduate.thesis.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Huy Pham
 */


@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
