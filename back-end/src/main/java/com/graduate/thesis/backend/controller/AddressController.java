package com.graduate.thesis.backend.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.address.AddressRequest;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.AddressService;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.ADDRESS_API)
public class AddressController extends AbstractBasedAPI{

    @Autowired
    AddressService addressService;

    @Autowired
    AddressAggregation addressAggregation;


    @GetMapping(value = Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getAddressById(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("id") String id
    )
    {

        AddressResponse address = addressAggregation
                .getAddressByAddressIdAndUserId(id,currentUser.getId());

        if(address == null){
            throw new ApplicationException(APIStatus.ERR_ADDRESS_NOT_FOUND);
        }

        return responseUtil.successResponse(address);
    }

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getAddressByUserId(
            @CurrentUser UserPrincipal currentUser
    )
    {

        List<AddressResponse> address = addressAggregation.getListAddressByUserId(currentUser.getId());

        return responseUtil.successResponse(address);
    }

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

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteAddress(
            @RequestParam("ids") String ids
    ){

        List<String> addressIds = Lists.newArrayList(Splitter.on(",").split(ids));

        addressAggregation.deleteAddressByIds(addressIds);

        return responseUtil.successResponse("OK");
    }

}
