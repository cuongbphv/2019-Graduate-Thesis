package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Pham
 */

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    @Query("{ '_id': ?0 }")
    Rating findRatingById(String id);

    Rating findBySenderIdAndRecipientId(String senderId, String recipientId);

}
