package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Pham
 */

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    Optional<UserAccount> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{ '_id': ?0 }")
    UserAccount findByUserId(String userId);

    @Query("{ '_id': ?0, 'status': ?1 }")
    UserAccount findByUserIdAndStatus(String userId, int status);

}
