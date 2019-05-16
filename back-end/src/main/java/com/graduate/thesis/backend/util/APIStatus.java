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

    //USER
    ERR_USER_NOT_FOUND(404, "User not found"),
    ERR_USER_PROFILE_NOT_FOUND(404, "User Profile Not Found"),
    ERR_USER_PROFILE_EXISTED(409, "User Profile Have Existed"),
    ERR_USER_ROLE_NOT_FOUND(404, "User Role Not Found"),
    ERR_USER_INCORRECT_PASSWORD(401, "Password is incorrect"),

    // LOCATION
    ERR_LOCATION_NOT_FOUND(504, "Location not found"),
    ERR_LOCATION_EXISTED(505, "Location has existed"),
    //FILE
    ERR_FILE_NOT_FOUND(888, "File not found"),
    ERR_CANT_STORE(889, "Could not store file"),
    ERR_FILE_TYPE(890, "Could not determine file type"),
    ERR_SMALL_IMAGE(891, "Image is small"),
    ERR_UPLOAD_FILE(892, "There is an error occur in upload process"),

    //CATEGORY
    ERR_CATEGORY_EXISTED(409, "Category is existed"),
    ERR_CATEGORY_NOT_FOUND(404, "Category not found"),

    //ADDRESS
    ERR_ADDRESS_NOT_FOUND(404, "Address not found for this user"),

    // Classified Advertising
    ERR_CLASSIFIED_ADVERTISING_NOT_FOUND(604, "Classified Advertising not found")
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
