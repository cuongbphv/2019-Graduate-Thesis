package com.graduate.thesis.backend.util;

import com.graduate.thesis.backend.model.response.RestAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huy Pham
 */

@Component
public class ResponseUtil {

    private RestAPIResponse _createResponse(APIStatus apiStatus, Object data) {
        return new RestAPIResponse(apiStatus, data);
    }

    // base method
    public ResponseEntity<RestAPIResponse> buildResponse(APIStatus apiStatus, Object data, HttpStatus httpStatus) {
        return new ResponseEntity(_createResponse(apiStatus, data), httpStatus);
    }

    public ResponseEntity<RestAPIResponse> successResponse(Object data) {
        return buildResponse(APIStatus.OK, data, HttpStatus.OK);
    }

    public ResponseEntity<RestAPIResponse> badRequestResponse(List<Constant.ParamError> errors) {

        Map<String, String> errMap = null;

        if (errors != null) {

            errMap = new HashMap<>();
            for (Constant.ParamError error : errors) {
                errMap.put(error.getName(), error.getDesc());
            }
        }

        return buildResponse(APIStatus.ERR_BAD_REQUEST, errMap, HttpStatus.BAD_REQUEST);
    }

}
