package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
public interface LocationService {

    void saveLocation(Location location);

    void saveListLocation(List<Location> locations);

    void deleteProvince(Location location);

    Location findByLocationId(String locationId);

    Page<Location> getPagingLocation(String searchKey, int sortKey, boolean ascSort, int pageSize, int pageNumber);

    List<District> getPagingDistrictsByProvinceId(
            String provinceId, String searchKey, int sortKey, boolean ascSort, int pageSize, int pageNumber);

}
