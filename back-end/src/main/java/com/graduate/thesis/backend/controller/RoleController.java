package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.entity.UserAccount;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.role_permission.RoleRequest;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.model.response.RoleResponse;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cuongbphv created on 19/05/2019
 */
@RestController
@RequestMapping(Constant.ROLE_API)
public class RoleController extends AbstractBasedAPI {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserAccountService userAccountService;

    @GetMapping()
    public ResponseEntity<RestAPIResponse> getListRoles (
            @CurrentUser UserPrincipal userPrincipal
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());

        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        List<Role> roles = roleService.findAllRole(Constant.Status.ACTIVE.getValue());

        List<RoleResponse> response = new ArrayList<>();

        for (Role role : roles) {
            List<Permission> permissions = permissionService.findByListId(role.getPermissions());
            response.add(new RoleResponse(role.getId(), role.getName(), role.getDescription(), permissions, role.getStatus()));
        }

        return responseUtil.successResponse(response);
    }

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createNewRole (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid RoleRequest roleRequest
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());

        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Role role = new Role();

        role.setName(roleRequest.getName());
        if (roleRequest.getDescription() != null) {
            role.setDescription(roleRequest.getDescription());
        }
        role.setPermissions(roleRequest.getPermissionIds());
        role.setStatus(Constant.Status.ACTIVE.getValue());

        return responseUtil.successResponse(roleService.saveRole(role));
    }


    @PutMapping()
    public ResponseEntity<RestAPIResponse> updateRoleById (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid RoleRequest roleRequest
            ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());

        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Role role = roleService.findByRoleId(roleRequest.getId());

        if (role == null) {
            throw new ApplicationException(APIStatus.ERR_ROLE_NOT_FOUND);
        }

        role.setName(roleRequest.getName());
        if (roleRequest.getDescription() != null) {
            role.setDescription(roleRequest.getDescription());
        }
        role.setPermissions(roleRequest.getPermissionIds());

        return responseUtil.successResponse(roleService.saveRole(role));
    }

    @DeleteMapping()
    public ResponseEntity<RestAPIResponse> updateRoleById (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "role_id") String roleId
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());

        if (!loginRole.getName().equals(Constant.Role.SYS_ADMIN.getName())) {
            throw new ApplicationException(APIStatus.ERR_UNAUTHORIZED);
        }

        Role role = roleService.findByRoleId(roleId);
        role.setStatus(Constant.Status.DELETE.getValue());
        roleService.saveRole(role);

        return responseUtil.successResponse("OK");
    }

}
