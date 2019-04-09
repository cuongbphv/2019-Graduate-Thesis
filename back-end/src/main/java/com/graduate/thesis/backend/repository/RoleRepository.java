package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Permission;
import com.graduate.thesis.backend.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Pham
 */

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    @Query("{ '_id': ?0 }")
    Role findByRoleId(String roleId);

}
