package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Rating;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.RatingRequest;
import com.graduate.thesis.backend.model.response.RatingResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.repository.RatingRepository;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.RatingService;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.RATING)
public class RatingController extends AbstractBasedAPI {

    @Autowired
    RatingService ratingService;

    @Autowired
    UserProfileService userProfileService;

    @GetMapping(Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getCurrentUserRating(
            @PathVariable("id") String recipientId,
            @CurrentUser UserPrincipal currentUser
            ){

        Rating existedRating = ratingService
                .findBySenderIdAndRecipientId(currentUser.getId(), recipientId);

        if(existedRating == null) {
            throw new ApplicationException(APIStatus.ERR_RATING_NOT_FOUND);
        }

        return responseUtil.successResponse(existedRating);
    }

    @GetMapping(Constant.WITHIN_ID + Constant.STATISTIC)
    public ResponseEntity<RestAPIResponse> getRatingStatistic(
            @PathVariable("id") String recipientId
    ){

        List<Rating> ratings = ratingService.findAllByRecipientId(recipientId);

        return responseUtil.successResponse(ratings);
    }

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getRatingsForUser(
            @RequestParam("user_id") String userId,
            @RequestParam("size") int size
    ){

        List<RatingResponse> ratings = ratingService.findByRecipientId(userId, size);

        return responseUtil.successResponse(ratings);
    }

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createRating(
            @RequestBody RatingRequest ratingRequest
            ){

        Rating existedRating = ratingService
                .findBySenderIdAndRecipientId(ratingRequest.getSenderId(), ratingRequest.getRecipientId());

        if(existedRating != null) {
            throw new ApplicationException(APIStatus.ERR_RATING_EXISTED);
        }

        Rating rating = new Rating();
        rating.setSenderId(ratingRequest.getSenderId());
        rating.setRecipientId(ratingRequest.getRecipientId());
        rating.setValue(ratingRequest.getValue());
        rating.setTitle(ratingRequest.getTitle());
        rating.setComment(ratingRequest.getComment());
        rating.setCreatedDate(new Date());
        rating.setModifiedDate(new Date());

        Rating createdRating = ratingService.save(rating);

        userProfileService.newRating(createdRating);

        return responseUtil.successResponse(createdRating);

    }


    @PutMapping(Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateRating(
            @PathVariable("id") String ratingId,
            @RequestBody RatingRequest ratingRequest
    ){

        Rating existedRating = ratingService.findById(ratingId);

        if(existedRating == null) {
            throw new ApplicationException(APIStatus.ERR_RATING_NOT_FOUND);
        }

        int oldValue = existedRating.getValue();

        existedRating.setValue(ratingRequest.getValue());
        existedRating.setTitle(ratingRequest.getTitle());
        existedRating.setComment(ratingRequest.getComment());
        existedRating.setModifiedDate(new Date());

        Rating updatedRating = ratingService.save(existedRating);

        userProfileService.updateRating(updatedRating, oldValue);

        return responseUtil.successResponse(updatedRating);

    }

    @DeleteMapping(Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> deleteRating(
            @PathVariable("id") String ratingId
    ){

        Rating existedRating = ratingService.findById(ratingId);

        if(existedRating == null) {
            throw new ApplicationException(APIStatus.ERR_RATING_NOT_FOUND);
        }

        ratingService.delete(existedRating);

        userProfileService.deleteRating(existedRating);

        return responseUtil.successResponse("OK");

    }

}
