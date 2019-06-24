package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.model.response.RatingResponse;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface RatingService {

    Rating save(Rating rating);

    Rating findById(String id);

    List<RatingResponse> findByRecipientId(String recipientId,  int size);

    Rating findBySenderIdAndRecipientId(String senderId, String recipientId);

    void delete(Rating rating);

}
