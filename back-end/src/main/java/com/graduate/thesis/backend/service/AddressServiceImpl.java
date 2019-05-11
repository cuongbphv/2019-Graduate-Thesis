package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Pham
 */

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address findAddressByIdAndStatus(String id, int status) {
        return addressRepository.findAddressByIdAndStatus(id, status);
    }
}
