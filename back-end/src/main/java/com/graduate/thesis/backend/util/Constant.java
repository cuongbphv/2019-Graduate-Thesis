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
    String GET_NEW_ITEM = "/new";
    String GET_TOP_SEARCH = "/trend";

    // Auth APIs
    String AUTH_API = API_PREFIX + "/auth";
    String REGISTRY_API =  "/register";
    String LOGIN_API =  "/login";
    String SUBMIT_OTP_API = "/otp";

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
    String FULL_TEXT_SEARCH = "/search";
    String TOP_CATEGORY_POST = "/top_category_post";
    String HISTORY = "/history";

    //USER
    String USER_API = API_PREFIX + "/user";
    String CURRENT_USER =  "/me";
    String CHANGE_PASSWORD =  "/password";

    //RATING
    String RATING = API_PREFIX + "/rating";
    String STATISTIC = "/statistic";

    // Role APIs
    String ROLE_API = API_PREFIX + "/role";

    // Permission APIs
    String PERMISSION_API = API_PREFIX + "/permission";

    //Profile management
    String PROFILE_API = API_PREFIX + "/profile";
    String AVATAR =  "/avatar";
    String SETTINGS =  "/settings";
    String PERSONAL_PERMISSION = "/permissions";
    String FOLLOW = "/follow";

    // Address APIs
    String ADDRESS_API = API_PREFIX + "/address";

    // Notification APIs
    String NOTIFICATION_API = API_PREFIX + "/notifications";

    // Chat APIs
    String CHAT_API = API_PREFIX + "/chats";
    String MESSAGE = "/messages";

    //FILE
    String FILE = API_PREFIX + "/files";

    int RANDOM_MIN = 100000;
    int RANDOM_MAX = 999999;
    int DEFAULT_EXPIRE_TIME = 3600000; // miliseconds (1 hour)
    int MAX_EXPIRE_TIME = 86400000; // miliseconds (1 day)
    String DEFAULT_EXPIRE_TIME_STR = "3600000"; // miliseconds (1 hour)
    int SALT_LENGTH = 6;
    String HEADER_TOKEN = "X-Access-Token";

    // ROLE
    enum Role{
        SYS_ADMIN("SYS_ADMIN"),
        ADMIN("ADMIN"),
        MODERATOR("MODERATOR"),
        USER("USER");

        private final String name;

        Role(String name){
            this.name = name;
        }

        public String getName(){
            return  this.name;
        }

    }

    enum Status{

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

    enum AdsType{

        BUY("BUY", 1),
        SELL("SELL", 2);

        private final String name;
        private final int value;

        AdsType(String name, int value){
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

    enum LocationType{

        DATA_ADDRESS("SELECT ADDRESS FROM DATA", 1),
        USER_ADDRESS("EXISTED ADDRESS OF USER", 2);

        private final String name;
        private final int value;

        LocationType(String name, int value){
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

    enum SystemRole {

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

    enum ParamError {
        ;

        private final String name;
        private final String desc;

        ParamError(String name, String desc) {
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


    enum ResetCodeType {

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
