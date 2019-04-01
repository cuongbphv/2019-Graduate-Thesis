package com.graduate.thesis.backend.util;

/**
 * @author Huy Pham
 */


public interface Constant {



    /**
     * ****************************
     * define AIPs path
     */
    public static final String API_PREFIX = "/api/v1";
    public static final String WITHIN_ID = "/{id}";

    // auth APIs
    public static final String AUTH_API = API_PREFIX + "/auth";
    public static final String REGISTRY_API =  "/register";
    public static final String LOGIN_API =  "/login";


    public static final String ADMIN_LOGIN_API = "/admin/login";
    public static final String ADMIN_GRANT_API = "/admin/grant/{code}";
    public static final String AUTH_ADMIN_INFO = "/admin";
    public static final String USER_LOGIN_API = "/login";
    public static final String SOCIAL_LOGIN_API = "/social_login";
    public static final String LOGOUT_API = "/logout";
    public static final String AUTH_USER_INFO = "/user";
    public static final String REFRESH_TOKEN = "/refresh";
    public static final String UPDATE_USER = "/update";
    public static final String CHECK_EMAIL = "/check";

    // Admin APIs
    public static final String ADMIN_API = API_PREFIX + "/admin";
    public static final String ADMIN_SETTING = "/setting";
    public static final String ADMIN_UPDATE_PASS = "/update_password";
    public static final String FILE = "/files";

    //User management
    public static final String USER_API = API_PREFIX + "/user";
    public static final String USER_REGISTER = "/signup";
    public static final String USER_LIST = "/list";
    public static final String USER_BANNED_LIST = "/list/banned";
    public static final String USER_DETAIL = "/detail";
    public static final String UPDATE_PASS = "/update_password";
    public static final String USER_UPDATE_PASS = "/user_update_password";
    public static final String USER_GRANT_ACCESS = "/grant";


    //Profile management
    public static final String PROFILE_API = API_PREFIX + "/profile";

    //Post management
    String ARTICLE_CONTROLLER = API_PREFIX + "/article";
    String LIST_ARTICLE = "/list";
    String LIST_UNAPPROVED_ARTICLE = "/list/unapproved";
    String LIST_REPORTED_ARTICLE = "/list/reported";
    String VIEW_COUNT = "/view";
    String STATS_BY_ARTICLE = "/stats";
    String MARK_AS_RESOLVED = "/resolved";
    String RELATED_ARTICLE_TAG = "/related";
    String THE_SAME_AUTHOR = "/same-author";
    String BOOKMARK_ARTICLE = "/bookmark";
    String TRENDING_ARTICLE = "trending";
    String APPROVE_POST = "/approve";
    String MY_ARTICLE = "/my-article";
    String MY_BOOKMARK = "/my-bookmark";

    // Article Interact
    String ARTICLE_INTERACT_CONTROLLER = API_PREFIX + "/interact";
    String LIST_ARTICLE_INTERACT = "/list";

    // Report Interact
    String REPORT_ARTICLE_CONTROLLER = API_PREFIX + "/report";

    // Report Comment
    String REPORT_COMMENT_CONTROLLER = API_PREFIX + "/report-comment";

    // Tag management
    String TAG_CONTROLLER = API_PREFIX + "/tag";
    String MOST_TAG_IN_FORUM = "/most";
    String LIST_TAG_BY_USER = "/list-tag";
    String MY_TAGS = "/my_tags";
    String LIST_TAG = "/list-detail";
    String LIST_BANNED_TAG = "/list/banned";
    String FOLLOW_TAG = "/follow";
    String FOLLOW_STATUS = "/status";
    String UNFOLLOW_TAG = "/unfollow";
    String LIST_FOLLOWER_BY_TAG = "/followers";
    String LIST_ARTICLE_BY_TYPE = "/list";
    String ALL_TAGS = "/all";

    // Author Management
    String AUTHOR_CONTROLLER = API_PREFIX + "/author";
    String TOP_AUTHOR = "/top";

    // Comment management
    String COMMENT_CONTROLLER = API_PREFIX + "/comment";
    String LIST_COMMENT_IN_ARTICLE = "/list";
    String COMMENT_INTERACT = "/interact";
    String MY_COMMENT= "/my-comment";
    String STAT_BY_COMMENT_ID = "/stats";

    // Search
    String SEARCH_CONTROLLER = API_PREFIX + "/search";
    String SEARCH_TOP_RESULT = "/top";
    String SEARCH_BY_TYPE = "/type";

    //Follow User
    public static final String FOLLOW_USER = "/follow";
    public static final String LIST_FOLLOW_USER = "/follow/list";
    public static final String LIST_FOLLOW_BY_OTHER = "/follow/list-followed";


    // Request Content Type
    public static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded";
    
    // Common header
    public static final String BOX_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-type";

    //Charset UTF
    public static final String CHARSET_UTF8 = "UTF-8";


    public static final int RANDOM_MIN = 100000;
    public static final int RANDOM_MAX = 999999;
    public static final int DEFAULT_EXPIRE_TIME = 3600000; // miliseconds (1 hour)
    public static final int MAX_EXPIRE_TIME = 86400000; // miliseconds (1 day)
    public static final String DEFAULT_EXPIRE_TIME_STR = "3600000"; // miliseconds (1 hour)
    public static final int SALT_LENGTH = 6;
    public static final String HEADER_TOKEN = "X-Access-Token";


    // Auth expire time
    public static final long DEFAULT_REMEMBER_LOGIN_MILISECONDS = 1296000000; // 15 days
    public static final long DEFAULT_SESSION_TIME_OUT = 1800000; // 30 minutes

    //Social Login
    public static final String CHECK_FACEBOOK_ACCESS_TOKEN_HEAD = "https://graph.facebook.com/debug_token?access_token=";
    public static final String CHECK_FACEBOOK_ACCESS_TOKEN_TAIL = "&input_token=";
    public static final String CHECK_GOOGLE_ACCESS_TOKEN = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";


    //sort case article management
    int SORT_BY_CREATE_DATE = 1;
    int SORT_BY_TYPE = 2;
    int SORT_BY_VIEW = 3;
    int SORT_BY_BOOKMARK = 4;

    //sort case article management
    int SORT_BY_BOOKMARK_DATE = 1;
    int SORT_BY_ARTICLE_CREATE_DATE = 2;

    //sort case user management
    int SORT_BY_FIRST_NAME = 2;
    int SORT_BY_LAST_NAME = 3;
    int SORT_BY_USERNAME = 4;
    int SORT_BY_ROLE = 5;

    //sort case tag management
    int SORT_BY_TAG_NAME = 2;

    public enum Status{

        DELETE("DELETE",0),
        ACTIVE("ACTIVE", 1),
        PENDING("PENDING",2),
        BLOCK("BLOCK",3);

        private final String name;
        private final int value;

        private Status(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }

    enum CommentInteractStatus{

        HEART("HEART", 1),
        UNHEART("UNHEART",0);

        private final String name;
        private final int value;

        CommentInteractStatus(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }

    enum ArticleType{

        ARTICLE("ARTICLE", 1),
        QUESTION("QUESTION",2);

        private final String name;
        private final int value;

        ArticleType(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }

    enum ArticleInteractType{
        BOOKMARK("bookmark", 1),
        RATING_DOWN("rating",-1),
        RATING_UP("rating",1),
        SHARE("share",1);

        private final String name;
        private final int value;

        ArticleInteractType(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }

    enum ApproveStatus{

        UNAPPROVED_STATUS("Unapproved Status", 0),
        APPROVED_STATUS("Approved Status", 1),
        REJECTED_STATUS("Rejected Status", 2),
        REPORTED_STATUS("Reported Status", 3)
        ;

        private final String name;
        private final int value;

        ApproveStatus(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }


    enum NotificationType{

        COMMENT("Comment", 1)
        ;

        private final String name;
        private final int value;

        NotificationType(String name, int value){
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return  this.name;
        }

        public int getValue(){
            return this.value;
        }
    }

    public enum SystemRole {

        SYS_ADMIN(1, "Admin"),
        USER(3, "User"),
        MODERATOR(2, "Moderator");

        private final int id;
        private final String name;

        ;

        private SystemRole(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getId(){ return id;}
    }

    public enum ParamError {
        ;

        private final String name;
        private final String desc;

        private ParamError(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }
    }


    public static enum ResetCodeType {

        FORGOT_PASS(0, "Forgot Pass"), CHANGE_EMAIL(1, "Change email");
        private final int value;
        private final String type;

        private ResetCodeType(int value, String type) {
            this.value = value;
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }

}
