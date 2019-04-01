package com.graduate.thesis.backend.security;

/**
 * @author Huy Pham
 */

public class SecurityConstants {

    public static final String SECRET = "926D96C90030DD58429D2751AC1BDBBC";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
   // public static final long EXPIRATION_TIME = 30000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SENSITIVE_HEADER = "Cookie,Set-Cookie";
    public static final String OAUTH2_REDIRECT_URI = "http://localhost:8080/login";

}
