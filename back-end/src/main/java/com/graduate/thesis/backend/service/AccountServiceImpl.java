package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Pham
 */

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findByUserId(String id) {
        return accountRepository.findByUserId(id);
    }
}
