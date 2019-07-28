package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Pham
 */

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Account findByUserId(String userId);

}
