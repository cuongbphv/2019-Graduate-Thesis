package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.repository.LocationRepository;
import com.graduate.thesis.backend.repository.aggregation.DistrictAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    DistrictAggregation districtAggregation;

    @Override
    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void saveListLocation(List<Location> locations) {
        locationRepository.saveAll(locations);
    }

    @Override
    public void deleteProvince(String id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void deleteListProvince(List<String> ids) {
        locationRepository.deleteByIdIn(ids);
    }

    @Override
    public Location findByLocationId(String locationId) {
        return locationRepository.findByLocationId(locationId);
    }

    @Override
    public Page<Location> getPagingLocation(String searchKey, int sortKey, boolean ascSort, int pageSize, int pageNumber) {

        if (searchKey.isEmpty()) {
            searchKey = " ";
        }

        return locationRepository.findLocationLikeName(
                searchKey, getPageLocation(sortKey, ascSort, pageNumber, pageSize));
    }

    @Override
    public List<District> getPagingDistrictsByProvinceId(
            String provinceId, String searchKey, int sortKey, boolean ascSort, int pageSize, int pageNumber) {

        if (searchKey.isEmpty()) {
            searchKey = " ";
        }

        return districtAggregation.getDistrictByProvinceId(provinceId, searchKey, sortKey, ascSort);
    }

    private Pageable getPageLocation(int sortKey, boolean ascSort, int pageNumber, int pageSize) {

        String properties = "";

        switch (sortKey) {
            case 1:
                properties = "_id"; break;
            case 2:
                properties = "name"; break;
            case 3:
                properties = "type"; break;
            default:
                properties = "_id";
        }

        return new PageRequest(pageNumber - 1, pageSize,
                ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);
    }
}
