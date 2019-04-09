package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huy Pham
 */

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public Permission findByPermissionId(String permissionId) {
        return permissionRepository.findByPermissionId(permissionId);
    }

    @Override
    public List<Permission> findByListId(List<String> ids) {
        return permissionRepository.findByListId(ids);
    }
}
