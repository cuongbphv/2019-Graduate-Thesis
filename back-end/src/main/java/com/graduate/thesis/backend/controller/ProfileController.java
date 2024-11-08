package com.graduate.thesis.backend.controller;

import com.auth0.exception.APIException;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.entity.model.UserSetting;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.ProfileRequest;
import com.graduate.thesis.backend.model.request.role_permission.PersonalPermissionsRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.service.RoleService;
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

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    RoleService roleService;

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

        profile.setCreatedDate(new Date());
        profile.setModifiedDate(new Date());
        profile.setStatus(Constant.Status.ACTIVE.getValue());

        if(profileRequest.getDob() != null && !profileRequest.getDob().trim().isEmpty()) {
            try {

                Date dob = new SimpleDateFormat("yyyy-MM-dd", new Locale("vi", "VN"))
                        .parse(profileRequest.getDob());

                profile.setDob(dob);

            } catch (ParseException e) {
                LOGGER.error("Could not parse DoB " + profileRequest.getDob(), e.getMessage());
            }
        }

        userProfileService.save(profile);

        return responseUtil.successResponse(profile);

    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserProfile(
            @CurrentUser UserPrincipal userPrincipal,
//            @RequestPart(value = "avatarImg", required = false) MultipartFile avatarImg,
//            @RequestPart(value = "profileRequest") ProfileRequest profileRequest
            @RequestBody ProfileRequest profileRequest
    ) {

        UserProfile profile = userProfileService.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));


        profile.setFirstName(profileRequest.getFirstName());
        profile.setLastName(profileRequest.getLastName());
        profile.setDescription(profileRequest.getDescription());
        profile.setGender(profileRequest.getGender());
        profile.setSocialLink(profileRequest.getSocialLink());
        profile.setModifiedDate(new Date());

        if(profileRequest.getDob() != null && !profileRequest.getDob().trim().isEmpty()) {
            try {
                Date dob = new SimpleDateFormat("yyyy-MM-dd", new Locale("vi", "VN"))
                        .parse(profileRequest.getDob());

                profile.setDob(dob);

            } catch (ParseException e) {
                LOGGER.error("Could not parse DoB", e.getMessage());
            }
        }

        userProfileService.save(profile);

        return responseUtil.successResponse(profile);
    }


    @RequestMapping(value = Constant.AVATAR, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserAvatar(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestPart(value = "avatar") MultipartFile avatarImg
    ) {

        UserProfile profile = userProfileService.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));


        if (avatarImg != null) {

            String fileName = "user_avatar_" + userPrincipal.getId() +
                    CommonUtil.getFileExtension(avatarImg);

            String url = fileUploadService.storeFile(avatarImg, fileName);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/files/")
                    .path(fileName)
                    .toUriString();

            profile.setAvatarUrl(fileDownloadUri);

        }

        userProfileService.save(profile);

        return responseUtil.successResponse(profile);
    }

    @RequestMapping(value = Constant.PERSONAL_PERMISSION, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserPermission(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid PersonalPermissionsRequest req
            ) {

        UserAccount account = userAccountService.findByUserId(userPrincipal.getId());

        if (account == null) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Role loginRole = roleService.findByRoleId(account.getRoleId());

        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        // change permission and role for user
        UserAccount userAccount = userAccountService.findByUserId(req.getUserId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        userAccount.setRoleId(req.getRoleId());
        if (req.getPermissionIds().size() > 0) {
            userAccount.setPersonalPermissions(req.getPermissionIds());
        }

        userAccountService.save(userAccount);

        return responseUtil.successResponse(userAccount);
    }


    @RequestMapping(value = Constant.SETTINGS, method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateUserSetting(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody UserSetting userSettingReq
    ) {

        UserProfile profile = userProfileService.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));

        profile.setSetting(userSettingReq);
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


    @RequestMapping(value = Constant.WITHIN_ID + Constant.FOLLOW, method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> followUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("id") String id
    )  {

        userProfileService.followUser(currentUser.getId(), id);

        return responseUtil.successResponse("OK");
    }

    @RequestMapping(value = Constant.WITHIN_ID + Constant.FOLLOW, method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> unFollowUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("id") String id
    )  {

        userProfileService.unFollowUser(currentUser.getId(), id);

        return responseUtil.successResponse("OK");
    }
}

