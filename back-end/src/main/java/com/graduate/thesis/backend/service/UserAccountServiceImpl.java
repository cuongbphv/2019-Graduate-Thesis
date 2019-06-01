package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.repository.UserAccountRepository;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Pham
 */

@Service
public class UserAccountServiceImpl implements UserAccountService{

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public UserAccount findByUserId(String userId) {
        return userAccountRepository.findByUserId(userId);
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount findActiveUserById(String userId) {
        return userAccountRepository.findByUserIdAndStatus(userId, Constant.Status.ACTIVE.getValue());
    }

    @Override
    public long countTotalAccount() {
        return userAccountRepository.count();
    }

}
