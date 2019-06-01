package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Address findAddressByIdAndUserIdAndStatus(String id, String userId, int status) {
        return addressRepository.findAddressByIdAndUserIdAndStatus(id, userId, status);
    }

    @Override
    public List<Address> findAddressByUserIdAndStatus(String userId, int status) {
        return addressRepository.findAddressByUserIdAndStatus(userId, status);
    }
}
