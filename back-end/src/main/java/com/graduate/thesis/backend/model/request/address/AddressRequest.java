package com.graduate.thesis.backend.model.request.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Huy Pham
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRequest {

    private String userId;

    private String provinceId;

    private String districtId;

    private String wardId;

    private String detail;

}
