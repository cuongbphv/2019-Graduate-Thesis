package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Huy Pham
 */

public interface SessionUserRepository extends CrudRepository<User, String> {

    User findByUsername(String username);

}
