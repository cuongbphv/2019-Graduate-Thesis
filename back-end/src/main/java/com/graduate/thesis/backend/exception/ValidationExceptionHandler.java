
package com.graduate.thesis.backend.exception;

import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Application exception handler
 *
 */
@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResponseUtil responseUtil;

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<RestAPIResponse> handleApplicationException(
            ApplicationException ex, WebRequest request) {

        LOGGER.debug("handleApplicationException", ex);

        ResponseEntity<RestAPIResponse> response;
        if (ex.getApiStatus() == APIStatus.ERR_BAD_REQUEST) {
            // handle bad request
            response = responseUtil.badRequestResponse(ex.getData());
        } else {
            response = responseUtil.buildResponse(ex.getApiStatus(), ex.getData(), HttpStatus.OK);
        }

        return response;
    }

    // when missing parameter
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        return new ResponseEntity<>(new RestAPIResponse<>(
                APIStatus.ERR_BAD_REQUEST, null), headers, status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestAPIResponse> handleUncatchException(Exception ex, WebRequest request) {

        LOGGER.error("handleUncatchException", ex);
        return responseUtil.buildResponse(APIStatus.ERR_INTERNAL_SERVER,
                "Please contact System Admin to resolve problem",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
