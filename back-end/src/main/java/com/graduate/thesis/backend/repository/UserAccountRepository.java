package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Pham
 */

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    UserAccount findByUsername(String username);

}
