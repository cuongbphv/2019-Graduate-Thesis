package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Pham
 */

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {

    @Query("{ '_id': ?0, 'status': ?1 }")
    Address findAddressByIdAndStatus(String id, int status);

    @Query("{ '_id': ?0, 'userId': ?1 , 'status': ?2 }")
    Address findAddressByIdAndUserIdAndStatus(String id, String userId, int status);

    List<Address> findAddressByUserIdAndStatus(String userId, int status);
}
