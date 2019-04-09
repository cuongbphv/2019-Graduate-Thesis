package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Role;
import com.graduate.thesis.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Pham
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByRoleId(String id) {
        return roleRepository.findByRoleId(id);
    }
}
