package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.repository.aggregation.LocationRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Repository
public interface LocationRepository extends MongoRepository<Location, String>,
        PagingAndSortingRepository<Location, String> {

    @Query("{ 'name': {$regex : ?0, $options: 'i'} }")
    Page<Location> findLocationLikeName(String name, Pageable pageable);

    @Query("{ '_id': ?0 }")
    Location findByLocationId(String locationId);

}
