package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.UserAccount;

/**
 * @author Huy Pham
 */

public interface UserAccountService {

    UserAccount findByUserId(String userId);

    UserAccount save(UserAccount userAccount);

    UserAccount findActiveUserById(String userId);

    long countTotalAccount();

}
