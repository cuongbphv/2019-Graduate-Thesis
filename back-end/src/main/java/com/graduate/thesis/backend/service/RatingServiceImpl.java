package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.model.response.RatingResponse;
import com.graduate.thesis.backend.repository.RatingRepository;
import com.graduate.thesis.backend.repository.aggregation.RatingAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    RatingAggregation ratingAggregation;

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating findById(String id) {
        return ratingRepository.findRatingById(id);
    }

    @Override
    public List<RatingResponse> findByRecipientId(String recipientId, int size) {
        return ratingAggregation.getByRecipientId(recipientId, size);
    }

    @Override
    public Rating findBySenderIdAndRecipientId(String senderId, String recipientId) {
        return ratingRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    }

    @Override
    public List<Rating> findAllByRecipientId(String recipientId) {
        return ratingRepository.findByRecipientId(recipientId);
    }

    @Override
    public void delete(Rating rating) {
        ratingRepository.delete(rating);
    }
}
