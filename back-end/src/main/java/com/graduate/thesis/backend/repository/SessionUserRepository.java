package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.SessionUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Huy Pham
 */

public interface SessionUserRepository extends CrudRepository<SessionUser, String> {

    SessionUser findByUsername(String username);

}
