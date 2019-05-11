package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Pham
 */

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {

    @Query("{ '_id': ?0, 'status': ?1 }")
    Address findAddressByIdAndStatus(String id, int status);
}
