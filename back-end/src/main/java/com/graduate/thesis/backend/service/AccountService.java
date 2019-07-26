package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.model.response.RatingResponse;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface AccountService {

    Account save(Account account);

    Account findByUserId(String id);
}
