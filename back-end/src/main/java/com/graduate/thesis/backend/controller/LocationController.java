package com.graduate.thesis.backend.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.entity.model.Ward;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.service.LocationService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import com.graduate.thesis.backend.util.UniqueID;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cuongbphv created on 25/04/2019
 */
@RestController
@RequestMapping(Constant.LOCATION_API)
public class LocationController extends AbstractBasedAPI {

    @Autowired
    LocationService locationService;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${file.data-dir}")
    private String dataPath;

    /**
     * brief: import location data from json file
     * @param jsonFile
     * @return
     */
    @PostMapping(Constant.IMPORT_LOCATION_DATA)
    public ResponseEntity<RestAPIResponse> importLocationData (
            @RequestPart("file") @Valid @NotNull MultipartFile jsonFile
    ) {
        try
        {
            String fileExtension = FilenameUtils.getExtension(jsonFile.getOriginalFilename());
            if (fileExtension == null) {
                throw new ApplicationException(APIStatus.ERR_FILE_TYPE);
            }
            String filePathName = UniqueID.getUUID() + "." + fileExtension.toLowerCase();
            // Folder to save file
            File folder = new File(dataPath);
            // Write file stream to server storage
            if (!folder.exists()) {
                // create the named directory.
                folder.mkdirs();
            }
            // New filepath
            File destFile = new File(folder, filePathName);
            //  Save Multipart buffer into file
            fileStorageService.appendFile(jsonFile.getInputStream(), destFile);
            // read json data from uploaded file
            FileReader reader = new FileReader(destFile);
            // create token from reader
            JSONTokener tokener = new JSONTokener(reader);
            // parse to json object from token
            JSONObject jsonObject = new JSONObject(tokener);
            // handle json data
            List<Location> locations = handleJsonProvinceData(jsonObject);
            // save list location to database
            locationService.saveListLocation(locations);
            //delete file
//            FileUtils.forceDelete(destFile);
            return responseUtil.successResponse("Upload successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(APIStatus.ERR_FILE_TYPE);
        }

    }

    @GetMapping
    public ResponseEntity<RestAPIResponse> getListPagingLocation (
            @RequestParam("search_key") String searchKey,
            @RequestParam("sort_key") int sortKey,
            @RequestParam("asc_sort") boolean ascSort,
            @RequestParam("page_size") int pageSize,
            @RequestParam("page_number") int pageNumber
    ) {

        Page<Location> locations = locationService.getPagingLocation(searchKey, sortKey, ascSort, pageSize, pageNumber);

        return responseUtil.successResponse(locations);
    }

    @GetMapping(Constant.PROVINCE_API + Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getListPagingDistrictByProvinceId (
            @PathVariable("id") String provinceId,
            @RequestParam("search_key") String searchKey,
            @RequestParam("sort_key") int sortKey,
            @RequestParam("asc_sort") boolean ascSort,
            @RequestParam("page_size") int pageSize,
            @RequestParam("page_number") int pageNumber
    ) {

        List<District> locations = locationService.getPagingDistrictsByProvinceId(
                provinceId, searchKey, sortKey, ascSort, pageSize, pageNumber);

        return responseUtil.successResponse(locations);
    }

    @DeleteMapping(Constant.PROVINCE_API)
    public ResponseEntity<RestAPIResponse> deleteProvinceByIds (
            @RequestParam("ids") String provinceIds
    ) {

        List<String> listProvinceIds = Lists.newArrayList(Splitter.on(",").split(provinceIds));
        locationService.deleteListProvince(listProvinceIds);

        return responseUtil.successResponse("OK");
    }

//    @DeleteMapping(Constant.PROVINCE_API + Constant.WITHIN_ID)
//    public ResponseEntity<RestAPIResponse> deleteProvinceById (
//            @PathVariable("id") String provinceId
//    ) {
//
//        Location location = locationService.findByLocationId(provinceId);
//
//        if (location == null) {
//            throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);
//        }
//
//        locationService.deleteProvince(provinceId);
//
//        return responseUtil.successResponse("OK");
//    }

    @DeleteMapping(Constant.DISTRICT_API)
    public ResponseEntity<RestAPIResponse> deleteDistrictByIds (
            @RequestParam("district_ids") String districtIds,
            @RequestParam("province_id") String provinceId
    ) {

        Location location = locationService.findByLocationId(provinceId);

        List<District> districts = location.getDistricts();
        List<District> newDistricts = new ArrayList<>();

        for(District district : districts) {
            if (!district.getId().equals(districtIds)) {
                newDistricts.add(district);
            }
        }

        location.setDistricts(newDistricts);
        locationService.saveLocation(location);

        return responseUtil.successResponse("OK");
    }

    private List<Location> handleJsonProvinceData(JSONObject jsonData) {
        List<Location> locations = new ArrayList<>();
        Iterator<String> provinceKeys = jsonData.keys();
        // iterator province by id
        while(provinceKeys.hasNext()) {
            Location location = new Location();
            String key = provinceKeys.next();
            location.setId(key);
            if (jsonData.get(key) instanceof JSONObject) {
                JSONObject provinceData = jsonData.getJSONObject(key);
                location.setName(provinceData.get("name").toString());
                location.setSlug(provinceData.get("slug").toString());
                location.setType(provinceData.get("type").toString());
                location.setNameWithType(provinceData.get("name_with_type").toString());
                location.setCode(provinceData.get("code").toString());
                location.setDistricts(handleJsonDistrictData(provinceData.getJSONObject("quan-huyen")));
            }
            locations.add(location);
        }
        return locations;
    }

    private List<District> handleJsonDistrictData(JSONObject jsonData) {
        List<District> districts = new ArrayList<>();
        Iterator<String> districtKeys = jsonData.keys();
        // iterator district by id
        while(districtKeys.hasNext()) {
            String key = districtKeys.next();
            District district = new District();
            district.setId(key);
            if (jsonData.get(key) instanceof JSONObject) {
                JSONObject districtData = jsonData.getJSONObject(key);
                district.setName(districtData.get("name").toString());
                district.setSlug(districtData.get("slug").toString());
                district.setType(districtData.get("type").toString());
                district.setNameWithType(districtData.get("name_with_type").toString());
                district.setPath(districtData.get("path").toString());
                district.setPathWithType(districtData.get("path_with_type").toString());
                district.setCode(districtData.get("code").toString());
                district.setParentCode(districtData.get("parent_code").toString());
                district.setWards(handleJsonWardData(districtData.getJSONObject("xa-phuong")));
            }
            districts.add(district);
        }
        return districts;
    }

    private List<Ward> handleJsonWardData(JSONObject jsonData) {
        List<Ward> wards = new ArrayList<>();
        Iterator<String> wardKeys = jsonData.keys();
        // iterator ward by id
        while(wardKeys.hasNext()) {
            Ward ward = new Ward();
            String key = wardKeys.next();
            ward.setId(key);
            if (jsonData.get(key) instanceof JSONObject) {
                JSONObject wardData = jsonData.getJSONObject(key);
                ward.setName(wardData.get("name").toString());
                ward.setSlug(wardData.get("slug").toString());
                ward.setPath(wardData.get("path").toString());
                ward.setPathWithType(wardData.get("path_with_type").toString());
                ward.setType(wardData.get("type").toString());
                ward.setNameWithType(wardData.get("name_with_type").toString());
                ward.setCode(wardData.get("code").toString());
                ward.setParentCode(wardData.get("parent_code").toString());
            }
            wards.add(ward);
        }
        return wards;
    }

}
