package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Address;

/**
 * @author Huy Pham
 */

public interface AddressService {

    Address save(Address address);

    Address findAddressByIdAndStatus(String id, int status);
}
