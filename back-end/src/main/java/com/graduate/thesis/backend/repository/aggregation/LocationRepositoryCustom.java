package com.graduate.thesis.backend.repository.aggregation;

import com.graduate.thesis.backend.entity.model.District;

import java.util.List;

/**
 * @author cuongbphv created on 29/04/2019
 */
public interface LocationRepositoryCustom {
    List<District> getDistrictByProvinceId(String provinceId, String searchKey, int sortKey, boolean ascSort);
}
