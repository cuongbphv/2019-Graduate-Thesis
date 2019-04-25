package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Location;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
public interface LocationService {

    void saveLocation(Location location);

    void saveListLocation(List<Location> locations);

//    Page<Location> getPagingLocation(String searchKey, int sortKey, boolean ascSort, int pageNumber, int pageSize);

}
