package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.entity.UserProfile;

import java.util.Optional;

/**
 * @author Huy Pham
 */


public interface UserProfileService {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findByUserId(String userId);

    Optional<UserProfile> findActiveByUserId(String userId);

    void deleteByUserId(String id);

    void followUser(String userId,String followedUserId);

    void unFollowUser(String userId,String followedUserId);

    void newRating(Rating rating);

    void updateRating(Rating newRating, int oldValue);

    void deleteRating(Rating rating);
}
