package com.graduate.thesis.backend.repository;

import com.graduate.thesis.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Repository
public interface LocationRepository extends MongoRepository<Location, String>, PagingAndSortingRepository<Location, String> {

//    Page<Location> findAllByNameLike(String name, Pageable pageable);
}
