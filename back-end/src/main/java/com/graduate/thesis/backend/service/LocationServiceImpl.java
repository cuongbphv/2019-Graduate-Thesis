package com.graduate.thesis.backend.service;

import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.entity.model.Ward;
import com.graduate.thesis.backend.repository.LocationRepository;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Async
    @Override
    public void deleteListProvince(List<String> ids) {
        for (String id: ids) {
            Location province = locationRepository.findByLocationId(id);
            if (province != null) {
                province.setStatus(Constant.Status.DELETE.getValue());
                locationRepository.save(province);
            }
        }
    }

    @Override
    public Location findByLocationId(String locationId) {
        return locationRepository.findByLocationId(locationId);
    }

    @Override
    public Location findByIdAndStatus(String id, int status) {
        return locationRepository.findByIdAndStatus(id, status);
    }

    @Override
    public Location findByIdOrName(String id, String name) {
        return locationRepository.findByIdOrName(id, name);
    }

    @Override
    public List<Location> fillAllLocation(String name) {
        List<Location> provinces = locationRepository.fillAllLocation(name,
                Constant.Status.ACTIVE.getValue(), new Sort(Sort.Direction.ASC, "name"));
        return mappingProvinces(provinces);
    }

    @Override
    public Page<Location> getPagingLocation(String searchKey, int sortKey, boolean ascSort, int pageNumber, int pageSize) {

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

        Sort sort = new Sort(ascSort ? Sort.Direction.ASC : Sort.Direction.DESC, properties);

        List<Location> mappedProvinces = mappingProvinces(locationRepository.fillAllLocation(
                searchKey, Constant.Status.ACTIVE.getValue(), sort));

        Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = (start + pageable.getPageSize()) > mappedProvinces.size() ? mappedProvinces.size() : (start + pageable.getPageSize());

        return new PageImpl<>(mappedProvinces.subList(start, end), pageable, mappedProvinces.size());
    }

    private List<Location> mappingProvinces(List<Location> provinces) {
        for (Location province : provinces) {
            List<District> districts = province.getDistricts().stream().filter(dis -> {
                if (dis.getStatus() == Constant.Status.ACTIVE.getValue()) {
                    List<Ward> wards = dis.getWards().stream().filter(ward ->
                            ward.getStatus() == Constant.Status.ACTIVE.getValue()).collect(Collectors.toList());
                    dis.setWards(wards);
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            province.setDistricts(districts);
        }

        return provinces;
    }
}
