package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Role;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface RoleService {

    Role saveRole(Role role);

    Role findByRoleId(String id);

    List<Role> findAllRole(int status);

}
