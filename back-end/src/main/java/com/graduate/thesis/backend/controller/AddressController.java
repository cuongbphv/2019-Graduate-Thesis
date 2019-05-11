package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.model.request.address.AddressRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.AddressService;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.ADDRESS_API)
public class AddressController extends AbstractBasedAPI{

    @Autowired
    AddressService addressService;


//    @GetMapping(value = Constant.WITHIN_ID)
//    public ResponseEntity<RestAPIResponse> getAddressById(
//            @CurrentUser UserPrincipal currentUser,
//            @PathVariable("id") String id
//    )
//    {
//
//    }


    @PostMapping()
    public ResponseEntity<RestAPIResponse> createAddress(
            @CurrentUser UserPrincipal currentUser,
            @RequestBody AddressRequest addressRequest
    )
    {

        Address address = new Address();
        address.setUserId(currentUser.getId());
        address.setProvinceId(addressRequest.getProvinceId());
        address.setDistrictId(addressRequest.getDistrictId());
        address.setWardId(addressRequest.getWardId());
        address.setDetail(addressRequest.getDetail());
        address.setStatus(Constant.Status.ACTIVE.getValue());

        Address createdAddress =  addressService.save(address);

        return responseUtil.successResponse(createdAddress);
    }


    @PutMapping(value = Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateAddress(
            @PathVariable("id") String id,
            @RequestBody AddressRequest addressRequest
    )
    {

        Address address = addressService.findAddressByIdAndStatus(id, Constant.Status.ACTIVE.getValue());

        address.setProvinceId(addressRequest.getProvinceId());
        address.setDistrictId(addressRequest.getDistrictId());
        address.setWardId(addressRequest.getWardId());
        address.setDetail(addressRequest.getDetail());

        Address updatedAddress =  addressService.save(address);

        return responseUtil.successResponse(updatedAddress);
    }

}
