package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.PermissionRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.PermissionService;
import com.graduate.thesis.backend.service.RoleService;
import com.graduate.thesis.backend.service.UserAccountService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author cuongbphv created on 20/05/2019
 */
@RestController
@RequestMapping(Constant.PERMISSION_API)
public class PermissionController extends AbstractBasedAPI {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserAccountService userAccountService;

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getListAllPermissions (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("search_key") String searchKey,
            @RequestParam("asc_sort") boolean ascSort
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        List<Permission> permissions = permissionService.findAllByStatus(
                searchKey, ascSort, Constant.Status.ACTIVE.getValue());

        return responseUtil.successResponse(permissions);
    }

    @PostMapping()
    public ResponseEntity<RestAPIResponse> addNewPermission (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @NotNull PermissionRequest permissionRequest
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Permission permission = new Permission();
        permission.setName(permissionRequest.getName());
        permission.setStatus(Constant.Status.ACTIVE.getValue());

        return responseUtil.successResponse(permissionService.savePermission(permission));
    }

    @PutMapping()
    public ResponseEntity<RestAPIResponse> editPermission (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @NotNull PermissionRequest permissionRequest
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Permission permission = permissionService.findByPermissionId(permissionRequest.getId());

        if (permission == null) {
            throw new ApplicationException(APIStatus.ERR_PERMISSION_NOT_FOUND);
        }

        permission.setName(permissionRequest.getName());

        return responseUtil.successResponse(permissionService.savePermission(permission));
    }

    @DeleteMapping()
    public ResponseEntity<RestAPIResponse> editPermission (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("permission_ids") String permissionIds
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        List<String> ids = Arrays.asList(permissionIds.split(","));

        for (String id : ids) {
            Permission permission = permissionService.findByPermissionId(id);
            if (permission != null) {
                permission.setStatus(Constant.Status.DELETE.getValue());
                permissionService.savePermission(permission);
            }
        }

        return responseUtil.successResponse("OK");
    }

}
