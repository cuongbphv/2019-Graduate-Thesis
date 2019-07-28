package com.graduate.thesis.backend.util;

/**
 * @author Huy Pham
 */

public enum APIStatus {

    OK(200, "OK"),
    //////////////////
    // CLIENT SIDE  //
    //////////////////
    ERR_BAD_REQUEST(400, "Bad request"),
    ERR_UNAUTHORIZED(401, "Unauthorized or Access Token is expired"),
    ERR_FORBIDDEN(403, "Forbidden! Access denied"),
    ERR_BAD_PARAMS(406, "Bad parameters"),
    ERR_ALREADY_EXISTED(407, "Already exsited."),
    //////////////////
    // SERVER SIDE  //
    //////////////////
    ERR_INTERNAL_SERVER(500, "Internal Server Error"),
    ERR_CREATE_MODEL(501, "Create model error"),

    //USER_ROLE_PERMISSION
    ERR_USER_NOT_FOUND(414, "User not found"),
    ERR_BALANCE_NOT_ENOUGH(419, "Balance not enough"),
    ERR_USER_PROFILE_NOT_FOUND(415, "User Profile Not Found"),
    ERR_PHONE_IN_USE(416, "Phone already in use"),
    ERR_USER_INCORRECT_PASSWORD(417, "Password or phone is incorrect"),
    ERR_USER_NOT_CONFIRM_OTP(420, "User has not confirmed OTP"),
    ERR_WRONG_OTP_CODE(421, "OTP Code is wrong"),
    ERR_IN_SENDING_OTP(422, "Error in sending OTP process"),
    ERR_USER_ACTIVE_NOW(423, "User is active now"),
    ERR_USER_USER_IS_FOLLOWED(207, "You have followed this user"),
    ERR_USER_USER_IS_NOT_FOLLOWED(208, "You have not followed this user yet"),
    ERR_USER_PROFILE_EXISTED(409, "User Profile Have Existed"),
    ERR_USER_ROLE_NOT_FOUND(404, "User Role Not Found"),
    ERR_PERMISSION_NOT_FOUND(554, "Permission not found"),
    ERR_ROLE_NOT_FOUND(564, "Role not found"),

    // LOCATION
    ERR_LOCATION_NOT_FOUND(504, "Location not found"),
    ERR_LOCATION_EXISTED(505, "Location has existed"),
    ERR_DISTRICT_NOT_FOUND(506, "District not found"),
    ERR_WARD_NOT_FOUND(507, "Ward not found"),

    //FILE
    ERR_FILE_NOT_FOUND(888, "File not found"),
    ERR_CANT_STORE(889, "Could not store file"),
    ERR_FILE_TYPE(890, "Could not determine file type"),
    ERR_SMALL_IMAGE(891, "Image is small"),
    ERR_UPLOAD_FILE(892, "There is an error occur in upload process"),

    //CATEGORY
    ERR_CATEGORY_EXISTED(409, "Category is existed"),
    ERR_CATEGORY_NOT_FOUND(404, "Category not found"),

    //CHAT
    ERR_CONVERSATION_EXISTED(409, "Conversation is existed"),
    ERR_CONVERSATION_NOT_EXISTED(404, "Conversation not found"),

    //ADDRESS
    ERR_ADDRESS_NOT_FOUND(404, "Address not found for this user"),

    //RATING
    ERR_RATING_EXISTED(209, "You have rate for this user"),
    ERR_RATING_NOT_FOUND(404, "Rating not found"),

    // Classified Advertising
    ERR_CLASSIFIED_ADVERTISING_NOT_FOUND(604, "Classified Advertising not found"),
    ERR_USER_REPORTED(605, "User reported classified advertising before"),
    ERR_CLASSIFIED_BLOCKED(606, "Classified Advertising blocked or deleted")
    ;


    private final int code;
    private final String description;

    private APIStatus(int s, String v) {
        code = s;
        description = v;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
