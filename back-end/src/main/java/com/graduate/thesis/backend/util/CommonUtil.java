package com.graduate.thesis.backend.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Huy Pham
 */


public class CommonUtil {

    public static final Random RANDOM = new SecureRandom();
    public final static ObjectMapper mapper = new ObjectMapper();

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateSalt() {
        byte[] salt = new byte[Constant.SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }


    public static boolean isEmailFormat(String valueToValidate) {
        // Regex
        String regexExpression = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?";
        Pattern regexPattern = Pattern.compile(regexExpression);
        boolean valid = false;
        if (valueToValidate != null) {
            Matcher matcher = regexPattern.matcher(valueToValidate);
            valid = matcher.matches();
        } else { // The case of empty Regex expression must be accepted
            Matcher matcher = regexPattern.matcher("");
            valid = matcher.matches();
        }
        return valid;
    }

    public static String getFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public static String getFileFormat(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

    public static String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }

}
