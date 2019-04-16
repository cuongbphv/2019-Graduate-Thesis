package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.repository.UserProfileRepository;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Huy Pham
 */

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;


    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserProfile> findActiveByUserId(String userId) {
        return userProfileRepository.findByUserIdAndStatus(userId, Constant.Status.ACTIVE.getValue());
    }

    @Override
    public void deleteByUserId(String userId) {
        userProfileRepository.deleteByUserId(userId);
    }
}
