package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.repository.ClassifiedAdvertisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuongbphv created on 15/05/2019
 */
@Service
public class ClassifiedAdvertisingServiceImpl implements ClassifiedAdvertisingService {

    @Autowired
    ClassifiedAdvertisingRepository classifiedAdvertisingRepository;

    @Override
    public ClassifiedAdvertising save(ClassifiedAdvertising classifiedAdvertising) {
        return classifiedAdvertisingRepository.save(classifiedAdvertising);
    }
}
