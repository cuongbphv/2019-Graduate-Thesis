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

    UserAccount findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("{ '_id': ?0 }")
    UserAccount findByUserId(String userId);

}
