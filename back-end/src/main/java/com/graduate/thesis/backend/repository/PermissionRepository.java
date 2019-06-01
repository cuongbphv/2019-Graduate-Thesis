package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Permission;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Pham
 */

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {

    @Query("{ '_id': ?0 }")
    Permission findByPermissionId(String permissionId);

    @Query("{'_id': { $in: ?0 } })")
    List<Permission> findByListId(List<String> ids);

    @Query("{ 'name': {$regex : ?0, $options: 'i'} , 'status' : ?1 }")
    List<Permission> findAllBySearchKeyAndStatus(String searchKey, int status, Sort sort);
}
