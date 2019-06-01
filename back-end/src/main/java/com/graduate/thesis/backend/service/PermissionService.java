package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Permission;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Huy Pham
 */

public interface PermissionService {

    Permission savePermission(Permission permission);

    Permission findByPermissionId(String permissionId);

    List<Permission> findByListId(List<String> ids);

    List<Permission> findAllByStatus(String searchKey, boolean ascSort, int status);

}
