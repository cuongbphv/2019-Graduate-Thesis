package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Pham
 */

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {


    Optional<UserProfile> findByUserId(String userId);

    Optional<UserProfile> findByUserIdAndStatus(String userId, int status);

//    @Query(value="{id : $0}", delete = true)
    void deleteByUserId(String userId);

}
