package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    @Query("{ '_id': ?0 }")
    Location findByLocationId(String locationId);

    Location findByIdAndStatus(String locationId, int status);

    @Query("{ 'name': {$regex : ?0, $options: 'i'}, 'status': ?1 }")
    List<Location> fillAllLocation(String name, int status, Sort sort);

    @Query("{ $or: [ { _id : ?0} , { name : ?1 } ] }")
    Location findByIdOrName(String id, String name);

}
