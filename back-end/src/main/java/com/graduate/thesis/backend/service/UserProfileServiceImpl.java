package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.repository.UserProfileRepository;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Huy Pham
 */

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    ClassifiedAdvertisingService classifiedAdvertisingService;


    @Override
    public UserProfile save(UserProfile userProfile) {

        classifiedAdvertisingService.reIndexProfile(userProfile);

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

    @Override
    public void followUser(String userId, String followedUserId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow( () ->
                new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND)
        );

        UserProfile followedProfile = userProfileRepository.findByUserId(followedUserId).orElseThrow( () ->
                new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND)
        );

        List<String> following = userProfile.getFollowing();
        List<String> followed = followedProfile.getFollowedBy();

        if(following == null){
            following = new ArrayList<>();
        }
        if(followed == null){
            followed = new ArrayList<>();
        }

        if( following.contains(followedUserId) || followed.contains(userId) ){
            throw new ApplicationException(APIStatus.ERR_USER_USER_IS_FOLLOWED);
        }

        following.add(followedUserId);
        followed.add(userId);

        userProfile.setFollowing(following);
        followedProfile.setFollowedBy(followed);

        userProfileRepository.save(userProfile);
        userProfileRepository.save(followedProfile);
    }

    @Override
    public void unFollowUser(String userId, String followedUserId) {

        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow( () ->
                new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND)
        );

        UserProfile followedProfile = userProfileRepository.findByUserId(followedUserId).orElseThrow( () ->
                new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND)
        );

        List<String> following = userProfile.getFollowing();
        List<String> followed = followedProfile.getFollowedBy();

        if( following == null || followed == null ||  !following.contains(followedUserId) || !followed.contains(userId) ){
            throw new ApplicationException(APIStatus.ERR_USER_USER_IS_NOT_FOLLOWED);
        }

        following.remove(followedUserId);
        followed.remove(userId);

        userProfile.setFollowing(following);
        followedProfile.setFollowedBy(followed);

        userProfileRepository.save(userProfile);
        userProfileRepository.save(followedProfile);
    }

    @Override
    public void newRating(Rating rating) {

        Optional<UserProfile> userProfile = findActiveByUserId(rating.getRecipientId());

        if(userProfile.isPresent()){

            UserProfile profile = userProfile.get();

            profile.setRatingCount(profile.getRatingCount() + 1);
            profile.setRatingValue(profile.getRatingValue() + rating.getValue());
            if(profile.getRatingCount() > 0) {
                double averageValue = profile.getRatingValue() * 1.0/ profile.getRatingCount();
                profile.setRatingAverage(averageValue);
            }
            else{
                profile.setRatingAverage(0);
            }

            save(profile);
        }
    }

    @Override
    public void updateRating(Rating rating, int oldValue) {

        Optional<UserProfile> userProfile = findActiveByUserId(rating.getRecipientId());

        if(userProfile.isPresent()){

            UserProfile profile = userProfile.get();

            profile.setRatingValue(profile.getRatingValue() - oldValue + rating.getValue());
            if(profile.getRatingCount() > 0) {
                double averageValue = profile.getRatingValue() * 1.0/ profile.getRatingCount();
                profile.setRatingAverage(averageValue);
            }
            else{
                profile.setRatingAverage(0);
            }

            save(profile);
        }

    }

    @Override
    public void deleteRating(Rating rating) {

        Optional<UserProfile> userProfile = findActiveByUserId(rating.getRecipientId());

        if(userProfile.isPresent()){

            UserProfile profile = userProfile.get();

            profile.setRatingCount(profile.getRatingCount() - 1);
            profile.setRatingValue(profile.getRatingValue() - rating.getValue());

            if(profile.getRatingCount() > 0) {
                double averageValue = profile.getRatingValue() * 1.0/ profile.getRatingCount();
                profile.setRatingAverage(averageValue);
            }
            else{
                profile.setRatingAverage(0);
            }

            save(profile);
        }

    }
}
