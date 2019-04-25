package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void saveListLocation(List<Location> locations) {
        locationRepository.saveAll(locations);
    }

//    @Override
//    public Page<Location> getPagingLocation(String searchKey, int sortKey, boolean ascSort, int pageNumber, int pageSize) {
//        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, new Sort(
//                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, sortKey));
//        return locationRepository.findAllByNameLike(searchKey, pageRequest);
//    }
}
