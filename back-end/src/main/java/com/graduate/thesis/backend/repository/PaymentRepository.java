package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Pham
 */

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    @Query("{ '_id': ?0 }")
    Payment findPaymentById(String id);

    List<Payment> findAllByUserId(String userId);

    List<Payment> findAllByUserIdAndStatus(String userId, String status);

}
