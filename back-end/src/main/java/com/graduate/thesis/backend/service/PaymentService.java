package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.entity.Payment;
import com.graduate.thesis.backend.entity.Rating;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface PaymentService {

    Payment findById(String id);

    Payment save(Payment payment);

    List<Payment> findByUserId(String id);

    List<Payment> findByUserIdAndStatus(String id, String status);
}
