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
    public static final String OAUTH2_REDIRECT_URI = "http://localhost:4040";
    public static final String FACEBOOK_CLIENT_ID = "2289113141355403";
    public static final String FACEBOOK_CLIENT_SECRET = "1681ffb65581998034aa24005c38af36";
    public static final String GOOGLE_CLIENT_ID = "1010672071642-fdlsc42jcl51btmi8jlvlm18kpa9i6ll.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "a69oFdUKQ8S-j6WANi3_SNW_";

    public static final String USER_ROLE_ID = "5c9bbbe605f5f129bd02cc83";

    // OTP
    public static final String OTP_URL = "https://sms4api.com/api/send";
    public static final String API_ID = "6de872fd-f89c-41e3-bb83-ca47344d947f";
    public static final String SECRET_KEY = "21f955e9d3";
    public static final String SMS_4_API = OTP_URL + "/" + SECRET_KEY + "/" + API_ID;

}
