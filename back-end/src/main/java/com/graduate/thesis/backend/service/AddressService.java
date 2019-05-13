package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Address;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface AddressService {

    Address save(Address address);

    Address findAddressByIdAndStatus(String id, int status);

    Address findAddressByIdAndUserIdAndStatus(String id, String userId, int status);

    List<Address> findAddressByUserIdAndStatus(String userId, int status);
}
