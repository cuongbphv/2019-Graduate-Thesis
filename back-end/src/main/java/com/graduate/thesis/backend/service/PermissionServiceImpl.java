package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission findByPermissionId(String permissionId) {
        return permissionRepository.findByPermissionId(permissionId);
    }

    @Override
    public List<Permission> findByListId(List<String> ids) {
        return permissionRepository.findByListId(ids);
    }

    @Override
    public List<Permission> findAllByStatus(String searchKey, boolean ascSort, int status) {

        Sort sort = new Sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, "name");

        return permissionRepository.findAllBySearchKeyAndStatus(searchKey, status, sort);
    }
}
