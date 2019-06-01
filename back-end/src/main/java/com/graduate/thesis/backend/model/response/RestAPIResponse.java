package com.graduate.thesis.backend.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.graduate.thesis.backend.util.APIStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Huy Pham
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestAPIResponse<T extends Object> implements Serializable {

    /**
     * status & message fields have not setter. They are assigned value when
     * initial by APIStatus parameter
     */
    private int status;
    private String message;
    private T data;

    public RestAPIResponse(APIStatus apiStatus, T data) {

        if (apiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = apiStatus.getCode();
        this.message = apiStatus.getDescription();
        this.data = data;
    }

}
