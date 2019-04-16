package com.graduate.thesis.backend.controller;

import com.auth0.exception.APIException;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.ProfileRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.service.UserAccountService;
import com.graduate.thesis.backend.service.UserProfileService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.CommonUtil;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.PROFILE_API)
public class ProfileController extends AbstractBasedAPI {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    FileStorageService fileUploadService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> createUserProfile(
            @RequestBody ProfileRequest profileRequest
    ) {

        if (userAccountService.findActiveUserById(profileRequest.getUserId()) == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        if (userProfileService.findByUserId(profileRequest.getUserId()).isPresent()) {
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_EXISTED);
        }

        UserProfile profile = new UserProfile();

        profile.setFirstName(profileRequest.getFirstName());
        profile.setLastName(profileRequest.getLastName());
        profile.setUserId(profileRequest.getUserId());
        profile.setGender(profileRequest.getGender());
        profile.setDescription(profileRequest.getDescription());
        profile.setAvatarUrl(profileRequest.getAvatarUrl());

        profile.setCreatedDate(plus1Day(new Date()));
        profile.setModifiedDate(plus1Day(new Date()));
        profile.setStatus(Constant.Status.ACTIVE.getValue());

        if(profileRequest.getDob() != null && !profileRequest.getDob().trim().isEmpty()) {
            try {

                Date dob = new SimpleDateFormat("MM/dd/yyyy", new Locale("vi", "VN"))
                        .parse(profileRequest.getDob());

                profile.setDob(plus1Day(dob));

            } catch (ParseException e) {
                LOGGER.error("Could not parse DoB " + profileRequest.getDob(), e.getMessage());
            }
        }

        userProfileService.save(profile);

        return responseUtil.successResponse(profile);

    }

    @RequestMapping(value = Constant.WITHIN_ID, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserProfile(
            @PathVariable("id") String userId,
            @RequestPart(value = "avatarImg", required = false) MultipartFile avatarImg,
            @RequestPart(value = "profileRequest") ProfileRequest profileRequest
    ) {

        UserProfile profile = userProfileService.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));


        profile.setFirstName(profileRequest.getFirstName());
        profile.setLastName(profileRequest.getLastName());
        profile.setDescription(profileRequest.getDescription());
        profile.setGender(profileRequest.getGender());
        profile.setSocialLink(profileRequest.getSocialLink());
        profile.setSetting(profileRequest.getSetting());
        profile.setModifiedDate(plus1Day(new Date()));

        if(profileRequest.getDob() != null && !profileRequest.getDob().trim().isEmpty()) {
            try {
                Date dob = new SimpleDateFormat("MM/dd/yyyy", new Locale("vi", "VN"))
                        .parse(profileRequest.getDob());

                profile.setDob(plus1Day(dob));

            } catch (ParseException e) {
                LOGGER.error("Could not parse DoB", e.getMessage());
            }
        }


        if (avatarImg != null) {

            String fileName = "user_avatar_" + profileRequest.getUserId() +
                    CommonUtil.getFileExtension(avatarImg);

            String url = fileUploadService.storeFile(avatarImg, fileName);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/")
                    .path(fileName)
                    .toUriString();

            profile.setAvatarUrl(fileDownloadUri);

        } else if (profileRequest.getAvatarUrl() != null) {

            profile.setAvatarUrl(profileRequest.getAvatarUrl());
        }

        userProfileService.save(profile);

        return responseUtil.successResponse(profile);
    }


    @RequestMapping(value = Constant.WITHIN_ID, method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getUserProfileByUserId(
            @PathVariable("id") String id
    )  {

        UserProfile profile = userProfileService.findByUserId(id)
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));

        return responseUtil.successResponse(profile);
    }


    /*
     *For fixing mongodb Date
     */
    Date plus1Day(Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);

        return c.getTime();
    }

}

