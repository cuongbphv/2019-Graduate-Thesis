package com.graduate.thesis.backend.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.graduate.thesis.backend.entity.Location;
import com.graduate.thesis.backend.entity.model.District;
import com.graduate.thesis.backend.entity.model.Ward;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.location.*;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.service.LocationService;
import com.graduate.thesis.backend.util.*;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${file.upload-dir}")
    private String uploadPath;

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
            String fileExtension = CommonUtil.getFileExtension(jsonFile);
            if (fileExtension.isEmpty()) {
                throw new ApplicationException(APIStatus.ERR_FILE_TYPE);
            }
            String filePathName = "location_" + UniqueID.getUUID() + fileExtension;
            // Folder to save file
            File folder = new File(uploadPath);
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
            FileUtil.deleteFile(uploadPath + filePathName);
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
            @RequestParam("page_number") int pageNumber,
            @RequestParam("page_size") int pageSize
    ) {

        Page<Location> locations = locationService.getPagingLocation(searchKey, sortKey, ascSort, pageNumber, pageSize);

        return responseUtil.successResponse(locations);
    }

    @GetMapping(Constant.GET_LIST)
    public ResponseEntity<RestAPIResponse> getListPagingLocation (
            @RequestParam("search_key") String searchKey
    ) {

        List<Location> locations = locationService.fillAllLocation(searchKey);

        return responseUtil.successResponse(locations);
    }

    @PostMapping(Constant.PROVINCE_API)
    public ResponseEntity<RestAPIResponse> addNewProvince(
            @RequestBody NewProvinceRequest newProvinceRequest
    ) {

        Location province = locationService.findByIdOrName(
                newProvinceRequest.getId(), newProvinceRequest.getName());

        // case new location
        if (province == null) {
            province = new Location();
        } else { // case id or name existed in database
            // case province deleted => can override and change status
            if (province.getStatus() == Constant.Status.ACTIVE.getValue()) {
                throw new ApplicationException(APIStatus.ERR_LOCATION_EXISTED);
            }
        }

        province.setId(newProvinceRequest.getId());
        province.setCode(newProvinceRequest.getId());
        province.setName(newProvinceRequest.getName());
        province.setSlug(newProvinceRequest.getSlug());
        province.setType(newProvinceRequest.getType());
        province.setNameWithType(newProvinceRequest.getNameWithType());
        province.setStatus(Constant.Status.ACTIVE.getValue());

        List<District> newDistricts = new ArrayList<>();

        for(DistrictRequest district : newProvinceRequest.getDistricts()) {
            // set data to new district
            District newDistrict = new District();
            newDistrict.setId(district.getId());
            newDistrict.setCode(district.getId());
            newDistrict.setType(district.getName());
            newDistrict.setSlug(district.getSlug());
            newDistrict.setName(district.getName());
            newDistrict.setNameWithType(district.getNameWithType());
            newDistrict.setParentCode(province.getId());
            newDistrict.setPath(district.getName() + ", " + province.getName());
            newDistrict.setPathWithType(district.getNameWithType() + ", " + province.getNameWithType());
            newDistrict.setStatus(Constant.Status.ACTIVE.getValue());

            List<Ward> newWards = new ArrayList<>();

            // map wards
            List<WardRequest> wards = district.getWards();
            for (WardRequest ward : wards) {
                Ward newWard = new Ward();
                newWard.setId(ward.getId());
                newWard.setName(ward.getName());
                newWard.setType(ward.getType());
                newWard.setCode(ward.getId());
                newWard.setSlug(ward.getSlug());
                newWard.setNameWithType(ward.getNameWithType());
                newWard.setParentCode(district.getId());
                newWard.setPath(ward.getName() + ", " + district.getName() + ", " + province.getName());
                newWard.setPath(ward.getNameWithType() + ", " + district.getNameWithType() + ", " + province.getNameWithType());
                newWard.setStatus(Constant.Status.ACTIVE.getValue());
                newWards.add(newWard);
            }

            newDistrict.setWards(newWards);
            newDistricts.add(newDistrict);
        }

        province.setDistricts(newDistricts);

        locationService.saveLocation(province);

        return responseUtil.successResponse("OK");
    }

    @PutMapping(Constant.PROVINCE_API + Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateProvinceById(
            @PathVariable("id") String provinceId,
            @RequestBody NewProvinceRequest req
    ) {

        Location province = locationService.findByLocationId(provinceId);

        if (province == null) throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);

        province.setName(req.getName());
        province.setSlug(req.getSlug());
        province.setType(req.getType());
        locationService.saveLocation(province);

        return responseUtil.successResponse("OK");
    }

    @DeleteMapping(Constant.PROVINCE_API)
    public ResponseEntity<RestAPIResponse> deleteProvinceByIds (
            @RequestParam("ids") String provinceIds
    ) {

        List<String> listProvinceIds = Lists.newArrayList(Splitter.on(",").split(provinceIds));
        locationService.deleteListProvince(listProvinceIds);

        return responseUtil.successResponse("OK");
    }

    @PostMapping(Constant.DISTRICT_API)
    public ResponseEntity<RestAPIResponse> addNewDistricts(
            @RequestBody NewDistrictRequest newDistrictRequest
    ) {

        Location location = locationService.findByIdAndStatus(
                newDistrictRequest.getProvinceId(), Constant.Status.ACTIVE.getValue());
        if (location == null) {
            throw new ApplicationException(APIStatus.ERR_LOCATION_EXISTED);
        }

        List<District> districts = location.getDistricts();
        for(DistrictRequest district : newDistrictRequest.getDistricts()) {
            // check if district have same id or same name and status
            District newDistrict = validateDistrict(district, districts);
            // in case district existed in database
            if (district == null) continue;
            // set data to new district
            newDistrict.setId(district.getId());
            newDistrict.setCode(district.getId());
            newDistrict.setType(district.getName());
            newDistrict.setSlug(district.getSlug());
            newDistrict.setName(district.getName());
            newDistrict.setNameWithType(district.getNameWithType());
            newDistrict.setParentCode(location.getId());
            newDistrict.setPath(district.getName() + ", " + location.getName());
            newDistrict.setPathWithType(district.getNameWithType() + ", " + location.getNameWithType());
            newDistrict.setStatus(Constant.Status.ACTIVE.getValue());

            // map wards
            List<Ward> newWards = new ArrayList<>();
            List<WardRequest> wards = district.getWards();
            for (WardRequest ward : wards) {
                Ward newWard = new Ward();
                newWard.setId(ward.getId());
                newWard.setName(ward.getName());
                newWard.setType(ward.getType());
                newWard.setCode(ward.getId());
                newWard.setSlug(ward.getSlug());
                newWard.setNameWithType(ward.getNameWithType());
                newWard.setParentCode(district.getId());
                newWard.setPath(ward.getName() + ", " + district.getName() + ", " + location.getName());
                newWard.setPath(ward.getNameWithType() + ", " + district.getNameWithType() + ", " + location.getNameWithType());
                newWard.setStatus(Constant.Status.ACTIVE.getValue());
                newWards.add(newWard);
            }

            newDistrict.setWards(newWards);
            districts.add(newDistrict);
        }

        location.setDistricts(districts);
        locationService.saveLocation(location);
        return responseUtil.successResponse("OK");
    }

    @PutMapping(Constant.DISTRICT_API + Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateProvinceById(
            @PathVariable("id") String districtId,
            @RequestBody NewDistrictRequest req
    ) {

        boolean isExisted = false;

        Location province = locationService.findByLocationId(req.getProvinceId());

        if (province == null) throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);

        for (District district: province.getDistricts()) {
            if (district.getId().equals(districtId)) {
                district.setName(req.getName());
                district.setSlug(req.getSlug());
                district.setType(req.getType());
                isExisted = true;
            }
        }

        if (!isExisted) throw new ApplicationException(APIStatus.ERR_DISTRICT_NOT_FOUND);

        locationService.saveLocation(province);
        return responseUtil.successResponse("OK");
    }

    @DeleteMapping(Constant.DISTRICT_API)
    public ResponseEntity<RestAPIResponse> deleteDistrictByIds (
            @RequestParam("district_ids") String districtIds,
            @RequestParam("province_id") String provinceId
    ) {

        Location location = locationService.findByLocationId(provinceId);
        if (location == null) throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);

        List<District> districts = location.getDistricts();
        List<String> listDistrictIds = Lists.newArrayList(Splitter.on(",").split(districtIds));
        for(District district : districts) {
            if (listDistrictIds.contains(district.getId())) {
                district.setStatus(Constant.Status.DELETE.getValue());
            }
        }

        locationService.saveLocation(location);

        return responseUtil.successResponse("OK");
    }

    @PostMapping(Constant.WARD_API)
    public ResponseEntity<RestAPIResponse> addNewWards(
            @RequestBody NewWardRequest newWardRequest
    ) {

        Location location = locationService.findByIdAndStatus(
                newWardRequest.getProvinceId(), Constant.Status.ACTIVE.getValue());

        if (location == null) {
            throw new ApplicationException(APIStatus.ERR_LOCATION_EXISTED);
        }

        Optional<District> optional = location.getDistricts().stream()
                .filter(district -> district.getId().equals(newWardRequest.getDistrictId()))
                .findFirst();

        if(!optional.isPresent()) {
            throw new ApplicationException(APIStatus.ERR_LOCATION_EXISTED);
        }

        District district = optional.get();
        List<Ward> wards = district.getWards();

        // map wards
        for (WardRequest ward : newWardRequest.getWards()) {
            Ward newWard = validateWard(ward, wards);
            // in case district existed in database
            if (newWard == null) continue;
            newWard.setId(ward.getId());
            newWard.setName(ward.getName());
            newWard.setType(ward.getType());
            newWard.setCode(ward.getId());
            newWard.setSlug(ward.getSlug());
            newWard.setNameWithType(ward.getNameWithType());
            newWard.setParentCode(district.getId());
            newWard.setPath(ward.getName() + ", " + district.getName() + ", " + location.getName());
            newWard.setPath(ward.getNameWithType() + ", " + district.getNameWithType() + ", " + location.getNameWithType());
            newWard.setStatus(Constant.Status.ACTIVE.getValue());
            wards.add(newWard);
        }

        district.setWards(wards);
        locationService.saveLocation(location);

        return responseUtil.successResponse("OK");
    }

    @PutMapping(Constant.WARD_API + Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateWardById(
            @PathVariable("id") String wardId,
            @RequestBody NewWardRequest req
    ) {

        boolean isExisted = false;

        Location province = locationService.findByLocationId(req.getProvinceId());

        if (province == null) throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);

        for (District district: province.getDistricts()) {
            if (district.getId().equals(req.getDistrictId())) {
                for (Ward ward : district.getWards()) {
                    if (ward.getId().equals(wardId)) {
                        ward.setName(req.getName());
                        ward.setSlug(req.getSlug());
                        ward.setType(req.getType());
                        isExisted = true;
                    }
                }
            }
        }

        if (!isExisted) throw new ApplicationException(APIStatus.ERR_WARD_NOT_FOUND);

        locationService.saveLocation(province);
        return responseUtil.successResponse("OK");
    }

    @DeleteMapping(Constant.WARD_API)
    public ResponseEntity<RestAPIResponse> deleteWardByIds (
            @RequestParam("ward_ids") String wardIds,
            @RequestParam("province_id") String provinceId,
            @RequestParam("district_id") String districtId
    ) {

        Location location = locationService.findByLocationId(provinceId);
        if (location == null) throw new ApplicationException(APIStatus.ERR_LOCATION_NOT_FOUND);

        List<District> districts = location.getDistricts();
        for(District district : districts) {
            if (district.getId().equals(districtId)) {
                List<String> listWardIds = Lists.newArrayList(Splitter.on(",").split(wardIds));
                for(Ward ward : district.getWards()) {
                    if (listWardIds.contains(ward.getId())) {
                        district.setStatus(Constant.Status.DELETE.getValue());
                    }
                }
            }
        }

        locationService.saveLocation(location);
        return responseUtil.successResponse("OK");
    }

    private District validateDistrict(DistrictRequest districtReq, List<District> districts) {
        for (District district : districts) {
            // case existed district
            if (districtReq.getId().equals(district.getId())) {
                if (district.getStatus() == Constant.Status.ACTIVE.getValue()) {
                    return null;
                }
                return district;
            }
        }
        return new District();
    }

    private Ward validateWard(WardRequest wardReq, List<Ward> wards) {
        for (Ward ward : wards) {
            // case existed district
            if (wardReq.getId().equals(ward.getId())) {
                if (ward.getStatus() == Constant.Status.ACTIVE.getValue()) {
                    return null;
                }
                return ward;
            }
        }
        return new Ward();
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
                location.setStatus(Constant.Status.ACTIVE.getValue());
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
                district.setStatus(Constant.Status.ACTIVE.getValue());
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
                ward.setStatus(Constant.Status.ACTIVE.getValue());
            }
            wards.add(ward);
        }
        return wards;
    }

}
