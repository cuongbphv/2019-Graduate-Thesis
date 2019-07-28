package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Payment;
import com.graduate.thesis.backend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public Payment findById(String id) {
        return paymentRepository.findPaymentById(id);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findByUserId(String id) {
        return paymentRepository.findAllByUserId(id);
    }

    @Override
    public List<Payment> findByUserIdAndStatus(String id, String status) {
        return paymentRepository.findAllByUserIdAndStatus(id, status);
    }
}
