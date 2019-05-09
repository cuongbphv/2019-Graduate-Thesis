package com.graduate.thesis.backend.util;

/**
 * @author Huy Pham
 */


public interface Constant {

    /**
     * ****************************
     * define AIPs path
     */
    String API_PREFIX = "/api/v1";
    String WITHIN_ID = "/{id}";

    // Common APIs
    String GET_LIST = "/list";

    // Auth APIs
    public static final String AUTH_API = API_PREFIX + "/auth";
    public static final String REGISTRY_API =  "/register";
    public static final String LOGIN_API =  "/login";

    // Category APIs
    String CATEGORY_API = API_PREFIX + "/category";
    String METADATA =  "/metadata";
    String FILTER = "/filter";
    String IMAGE = "/image";

    // Location APIs
    String LOCATION_API = API_PREFIX + "/location";
    String PROVINCE_API = "/province";
    String DISTRICT_API = "/district";
    String WARD_API = "/ward";
    String IMPORT_LOCATION_DATA = "/import";

    // Advertising APIs
    String ADVERTISING_API = API_PREFIX + "/advertising";
    String UPLOAD_TEMP_IMAGE = "/upload_temp_image";
    String REMOVE_TEMP_IMAGE = "/remove_temp_image";

    //USER
    public static final String USER_API = API_PREFIX + "/user";
    public static final String CURRENT_USER =  "/me";
    public static final String CHANGE_PASSWORD =  "/password";

    //Profile management
    public static final String PROFILE_API = API_PREFIX + "/profile";
    public static final String AVATAR =  "/avatar";
    public static final String SETTINGS =  "/settings";



    //FILE
    public static final String FILE = API_PREFIX + "/files";

    public static final int RANDOM_MIN = 100000;
    public static final int RANDOM_MAX = 999999;
    public static final int DEFAULT_EXPIRE_TIME = 3600000; // miliseconds (1 hour)
    public static final int MAX_EXPIRE_TIME = 86400000; // miliseconds (1 day)
    public static final String DEFAULT_EXPIRE_TIME_STR = "3600000"; // miliseconds (1 hour)
    public static final int SALT_LENGTH = 6;
    public static final String HEADER_TOKEN = "X-Access-Token";


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
