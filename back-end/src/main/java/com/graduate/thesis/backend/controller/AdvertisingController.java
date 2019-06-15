package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.*;
import com.graduate.thesis.backend.entity.elastic.ClassifiedAdvertisingElastic;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.advertising.AdsMetadata;
import com.graduate.thesis.backend.model.request.advertising.NewAdvertisingRequest;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.model.response.ClassifiedAdvertisingPagingResponse;
import com.graduate.thesis.backend.model.response.NewAdsPagingResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.model.response.advertising.NewAds;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.repository.aggregation.LocationAggregation;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.*;
import com.graduate.thesis.backend.service.elastic.ClassifiedAdvertisingElasticService;
import com.graduate.thesis.backend.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 * @author cuongbphv created on 08/05/2019
 */
@RestController
@RequestMapping(Constant.ADVERTISING_API)
public class AdvertisingController extends AbstractBasedAPI {

    @Value("${file.upload-dir}")
    private String uploadPath;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ClassifiedAdvertisingService classifiedAdvertisingService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    AddressAggregation addressAggregation;

    @Autowired
    LocationAggregation locationAggregation;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    RoleService roleService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ClassifiedAdvertisingElasticService classifiedAdvertisingElasticService;


    @GetMapping(Constant.FULL_TEXT_SEARCH)
    public ResponseEntity<RestAPIResponse> fullTextSearch(
            @RequestParam("search_key") String searchKey,
            @RequestParam(value = "category_id", required = false, defaultValue = "") String categoryId,
            @RequestParam(value = "sort_key", required = false, defaultValue = "createdDate") String sortKey,
            @RequestParam(value = "asc_sort", required = false, defaultValue = "false") boolean ascSort,
            @RequestParam("page_size") int pageSize,
            @RequestParam("page_number") int pageNumber,
            @RequestParam("min_price") double minPrice,
            @RequestParam("max_price") double maxPrice,
            @RequestParam(value = "location_id", required = false, defaultValue = "") String locationId,
            @RequestParam Map<String, String> metadata
    ){

//        List<AdsMetadata> adsMetadata = new ArrayList<>();
//
//        if(adsMetadata == null){
//            adsMetadata = new ArrayList<>();
//        }

        metadata.remove("search_key");
        metadata.remove("category_id");
        metadata.remove("sort_key");
        metadata.remove("asc_sort");
        metadata.remove("page_size");
        metadata.remove("page_number");
        metadata.remove("min_price");
        metadata.remove("max_price");
        metadata.remove("location_id");

        ClassifiedAdvertisingPagingResponse result =
                classifiedAdvertisingElasticService.fullTextSearch(
                        categoryId,
                        searchKey,
                        locationId,
                        metadata,
                        pageNumber - 1,
                        pageSize,
                        sortKey,
                        ascSort,
                        minPrice,
                        maxPrice
                        );

        return responseUtil.successResponse(result);

    }


    @PostMapping()
    public ResponseEntity<RestAPIResponse> createNewAdvertising (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody NewAdvertisingRequest reqModel
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        // case guest
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        ClassifiedAdvertising classifiedAdvertising = new ClassifiedAdvertising();
        classifiedAdvertising.setLocationType(reqModel.getLocationType());
        if (reqModel.getLocationType() == Constant.LocationType.DATA_ADDRESS.getValue()) {
            classifiedAdvertising.setProvinceId(reqModel.getProvinceId());
            classifiedAdvertising.setDistrictId(reqModel.getDistrictId());
            classifiedAdvertising.setWardId(reqModel.getWardId());
        } else if (reqModel.getLocationType() == Constant.LocationType.USER_ADDRESS.getValue()) {
            classifiedAdvertising.setAddressId(reqModel.getAddressId());
        }
        classifiedAdvertising.setAuthorId(userPrincipal.getId());
        classifiedAdvertising.setImages(reqModel.getImages());
        classifiedAdvertising.setAdditionalInfo(reqModel.getAdditionalInfo());
        classifiedAdvertising.setBreadcrumbs(reqModel.getBreadcrumbs());
        classifiedAdvertising.setMetadata(reqModel.getMetadata());
        classifiedAdvertising.setCreatedDate(new Date());
		classifiedAdvertising.setModifiedDate(new Date());
        // just for new after that will update pending status, after previewer confirm switch to active status
        classifiedAdvertising.setStatus(Constant.Status.PENDING.getValue());

        ClassifiedAdvertising createdAds = classifiedAdvertisingService.save(classifiedAdvertising);

        indexAds(createdAds);

        Notification notification = new Notification();
        notification.setCreatedDate(new Date());
        notification.setSenderId(userPrincipal.getId());
        notification.setType(Notification.Type.NEW_POST);
        notification.setData(createdAds);

        notificationService.save(notification);

        return responseUtil.successResponse(createdAds);
    }

    @PutMapping(value = Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> updateStatusClassifiedAds (
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String adsId,
            @RequestParam("status") int status
    ) {

        ClassifiedAdvertising classifiedAdvertising;

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        // case guest
        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
        // check role admin or moderator can see classified advertising
        if (loginRole.getName().equals(Constant.Role.ADMIN.getName())
                || loginRole.getName().equals(Constant.Role.MODERATOR.getName())) {
            classifiedAdvertising = classifiedAdvertisingService
                    .getClassifiedAdsDetail(adsId, Arrays.asList(
                            Constant.Status.ACTIVE.getValue(), // see normal post
                            Constant.Status.PENDING.getValue(), // see before accept post
                            Constant.Status.BLOCK.getValue(), // see after block post
                            Constant.Status.DELETE.getValue() // check reason delete post
                    ));

            // update status
            if (classifiedAdvertising.getStatus() != Constant.Status.DELETE.getValue()) {
                classifiedAdvertising.setStatus(status);
            }
            classifiedAdvertisingService.save(classifiedAdvertising);

            return responseUtil.successResponse(classifiedAdvertising);
        }

        throw new ApplicationException(APIStatus.ERR_CLASSIFIED_ADVERTISING_NOT_FOUND);
    }

    @GetMapping(value = Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getClassifiedAdsDetail (
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable("id") String adsId
    ) {

        ClassifiedAdvertising classifiedAdvertising;

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());
        // case guest
        if (userAccount == null) {
            classifiedAdvertising = classifiedAdvertisingService
                    .getClassifiedAdsDetail(adsId, Arrays.asList(Constant.Status.ACTIVE.getValue()));
        } else { // case member
            Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
            // check role admin or moderator can see classified advertising
            if (loginRole.getName().equals(Constant.Role.ADMIN.getName())
                    || loginRole.getName().equals(Constant.Role.MODERATOR.getName())) {
                classifiedAdvertising = classifiedAdvertisingService
                        .getClassifiedAdsDetail(adsId, Arrays.asList(
                                Constant.Status.ACTIVE.getValue(), // see normal post
                                Constant.Status.PENDING.getValue(), // see before accept post
                                Constant.Status.BLOCK.getValue(), // see after block post
                                Constant.Status.DELETE.getValue() // check reason delete post
                        ));
            } else { // case normal member
                // who logged in is ads owner
                classifiedAdvertising = classifiedAdvertisingService.getClassifiedAdsDetailByIdAndAuthor(
                        adsId, userAccount.getId());

                if (classifiedAdvertising == null) {
                    // who logged in is not ads owner
                    classifiedAdvertising = classifiedAdvertisingService
                            .getClassifiedAdsDetail(adsId, Arrays.asList(Constant.Status.ACTIVE.getValue()));
                }
            }
        }

        if (classifiedAdvertising == null) {
            throw new ApplicationException(APIStatus.ERR_CLASSIFIED_ADVERTISING_NOT_FOUND);
        }

        // get address
        AddressResponse authorAddress = null;
        if (classifiedAdvertising.getLocationType() == Constant.LocationType.DATA_ADDRESS.getValue()) {
            authorAddress = locationAggregation.getAddressByLocationId(
                    classifiedAdvertising.getProvinceId(),
                    classifiedAdvertising.getDistrictId(),
                    classifiedAdvertising.getWardId()
            );
        } else if (classifiedAdvertising.getLocationType() == Constant.LocationType.USER_ADDRESS.getValue()) {
            authorAddress = addressAggregation.getAddressByAddressIdAndUserId(
                    classifiedAdvertising.getAddressId(),
                    classifiedAdvertising.getAuthorId()
            );
        }

        // get breadcrumb
        List<Category> breadcrumbs = new ArrayList<>();

        for (String id : classifiedAdvertising.getBreadcrumbs()) {
            Category category = categoryService.findCategoryByIdAndStatus(id, Constant.Status.ACTIVE.getValue());
            breadcrumbs.add(category);
        }

        // get author info
        Optional<UserProfile> userProfile = userProfileService
                .findByUserId(classifiedAdvertising.getAuthorId());

        HashMap<String, Object> response = new HashMap<>();
        response.put("breadcrumbs", breadcrumbs);
        response.put("detail", classifiedAdvertising);
        if (userProfile.isPresent()) {
            response.put("author", userProfile.get());
        } else {
            response.put("author", "Tài khoản bị khóa");
        }
        response.put("address", authorAddress);

        return responseUtil.successResponse(response);
    }

    @GetMapping(value = Constant.CURRENT_USER)
    public ResponseEntity<RestAPIResponse> getUserHistoryClassifiedAdsPaging (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page_number", defaultValue = "1") int pageNumber,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(value = "search_key", defaultValue = "") String searchKey,
            @RequestParam(value = "asc_sort", defaultValue = "true") boolean ascSort,
            @RequestParam(value = "status", defaultValue = "-1") int status,
            @RequestParam(value = "category_id", defaultValue = "") String categoryId
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        if (userAccount == null) {
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        Page<ClassifiedAdvertising> page = classifiedAdvertisingService.getPagingAdsByAuthorId(
                userAccount.getId(),
                searchKey,
                categoryId,
                ascSort,
                pageNumber,
                pageSize,
                (status != -1) ? Arrays.asList(status) : Arrays.asList(
                        Constant.Status.ACTIVE.getValue(), // see normal post
                        Constant.Status.PENDING.getValue(), // see before accept post
                        Constant.Status.BLOCK.getValue(), // see after block post
                        Constant.Status.DELETE.getValue() // check reason delete post
                ));

        return responseUtil.successResponse(mappingClassifiedAds(page));
    }

    @GetMapping(value = Constant.GET_NEW_ITEM)
    public ResponseEntity<RestAPIResponse> getNewClassifiedAdsPaging (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page_number", defaultValue = "1") int pageNumber,
            @RequestParam(value = "page_size", defaultValue = "4") int pageSize,
            @RequestParam(value = "search_key", defaultValue = "") String searchKey,
            @RequestParam(value = "sort_key", defaultValue = "1") int sortKey,
            @RequestParam(value = "asc_sort", defaultValue = "true") boolean ascSort,
            @RequestParam(value = "province_id", defaultValue = "") String provinceId,
            @RequestParam(value = "district_id", defaultValue = "") String districtId,
            @RequestParam(value = "ward_id", defaultValue = "") String wardId,
            @RequestParam(value = "status", defaultValue = "-1") int status,
            @RequestParam(value = "category_id", defaultValue = "") String categoryId
    ) {

        UserAccount userAccount = userAccountService.findActiveUserById(userPrincipal.getId());

        Page<ClassifiedAdvertising> page;

        if (userAccount == null) {
            page = classifiedAdvertisingService.getPagingNewAds(
                    searchKey, provinceId, districtId, wardId, categoryId,
                    sortKey, ascSort, pageNumber, pageSize,
                    (status != -1) ? Arrays.asList(status) : Arrays.asList(Constant.Status.ACTIVE.getValue()));
        } else {
            Role loginRole = roleService.findByRoleId(userAccount.getRoleId());
            // check role admin or moderator can see classified advertising
            if (loginRole.getName().equals(Constant.Role.ADMIN.getName())
                    || loginRole.getName().equals(Constant.Role.MODERATOR.getName())) {
                page = classifiedAdvertisingService.getPagingNewAds(
                        searchKey, provinceId, districtId, wardId, categoryId,
                        sortKey, ascSort, pageNumber, pageSize,
                        (status != -1) ? Arrays.asList(status) : Arrays.asList(
                                Constant.Status.ACTIVE.getValue(), // see normal post
                                Constant.Status.PENDING.getValue(), // see before accept post
                                Constant.Status.BLOCK.getValue(), // see after block post
                                Constant.Status.DELETE.getValue() // check reason delete post
                        ));
            } else {
                page = classifiedAdvertisingService.getPagingNewAds(
                        searchKey, provinceId, districtId, wardId, categoryId,
                        sortKey, ascSort, pageNumber, pageSize,
                        (status != -1) ? Arrays.asList(status) : Arrays.asList(Constant.Status.ACTIVE.getValue()));
            }
        }

        return responseUtil.successResponse(mappingClassifiedAds(page));
    }

    private NewAdsPagingResponse mappingClassifiedAds(Page<ClassifiedAdvertising> page) {
        List<NewAds> listContent = new ArrayList<>();

        for (ClassifiedAdvertising classifiedAdvertising : page.getContent()) {

            NewAds newAds = new NewAds();
            newAds.setId(classifiedAdvertising.getId());
            newAds.setClassifiedAdsId(classifiedAdvertising.getId());
            newAds.setTitle(classifiedAdvertising.getAdditionalInfo().getTitle());
            newAds.setCreatedDate(classifiedAdvertising.getCreatedDate());
            newAds.setThumbnail(classifiedAdvertising.getImages().get(0));
            newAds.setStatus(classifiedAdvertising.getStatus());

            AddressResponse authorAddress = null;
            if (classifiedAdvertising.getLocationType() == Constant.LocationType.DATA_ADDRESS.getValue()) {
                authorAddress = locationAggregation.getAddressByLocationId(classifiedAdvertising.getProvinceId(),
                        classifiedAdvertising.getDistrictId(), classifiedAdvertising.getWardId());
            } else if (classifiedAdvertising.getLocationType() == Constant.LocationType.USER_ADDRESS.getValue()) {
                authorAddress = addressAggregation.getAddressByAddressIdAndUserId(classifiedAdvertising.getAddressId(),
                        classifiedAdvertising.getAuthorId()
                );
            }
            newAds.setAddressResponse(authorAddress);

            // get breadcrumb
            Category category = categoryService.findCategoryByIdAndStatus(classifiedAdvertising.getBreadcrumbs().get(0),
                    Constant.Status.ACTIVE.getValue());

            newAds.setCategoryId(category.getId());
            newAds.setCategoryName(category.getName());

            // get author info
            Optional<UserProfile> userProfile = userProfileService
                    .findByUserId(classifiedAdvertising.getAuthorId());

            if (userProfile.isPresent()) {
                newAds.setAuthorId(userProfile.get().getId());
                newAds.setFirstName(userProfile.get().getFirstName());
                newAds.setLastName(userProfile.get().getLastName());
                newAds.setAvatarUrl(userProfile.get().getAvatarUrl());
            }

            listContent.add(newAds);
        }

        NewAdsPagingResponse response = new NewAdsPagingResponse();
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setContent(listContent);

        return response;
    }

    @PostMapping(value = Constant.UPLOAD_TEMP_IMAGE)
    public ResponseEntity<RestAPIResponse> uploadTempImage(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "file") MultipartFile tempImage
    ) {

        String ext = CommonUtil.getFileExtension(tempImage);
        String fileName = "temp_" + userPrincipal.getId() + ext;
        String url = fileStorageService.storeFile(tempImage, fileName);

        // create logo watermark
        String urlWatermark = makeWaterMark(userPrincipal.getId(), url, ext);
        if (urlWatermark.isEmpty()) {
            throw new ApplicationException(APIStatus.ERR_UPLOAD_FILE);
        }
        FileUtil.deleteFile(url);

        // compressed image
        String resUrl = compressImage(urlWatermark, userPrincipal.getId(), ext);
        if (resUrl.isEmpty()) {
            throw new ApplicationException(APIStatus.ERR_UPLOAD_FILE);
        }
        FileUtil.deleteFile(urlWatermark);

        // create access link
        String accessLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/")
                .path(resUrl)
                .toUriString();

        return responseUtil.successResponse(accessLink);
    }

    @DeleteMapping(value = Constant.REMOVE_TEMP_IMAGE)
    public ResponseEntity<RestAPIResponse> deleteTempImage(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "file_name") String fileName
    ) {
        FileUtil.deleteFile(uploadPath + fileName);

        return responseUtil.successResponse("OK");
    }

    private String makeWaterMark(String userId, String src, String format) {
        try {
            // Read image uploaded
            BufferedImage srcImage = ImageIO.read(new File(src));
            int width = srcImage.getWidth(null);
            int height = srcImage.getHeight(null);

            if (width < 500 || height < 750) {
                throw new ApplicationException(APIStatus.ERR_SMALL_IMAGE);
            }

            // Draw Watermark
            BufferedImage destImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = destImage.getGraphics();
            g.drawImage(srcImage, 0, 0, width, height, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("BeeMARKET", width - 200, height - 20);
            g.dispose();

            // Write new image
            String fileName = UniqueID.getUUID() + userId + format;
            String url = uploadPath + fileName;
            File f = new File(url);
            if (f.getParentFile() == null) {
                f.getParentFile().mkdirs();
            }
            ImageIO.write(destImage, CommonUtil.getFileFormat(format), f);
            return url;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private String compressImage(String inputFile, String userId, String format) {
        try {
            BufferedImage image = ImageIO.read(new File(inputFile));

            String fileName = UniqueID.getUUID() + userId + format;
            String url = uploadPath + "compressed_" + fileName;

            File output = new File(url);
            OutputStream out = new FileOutputStream(output);

            ImageWriter writer =  ImageIO.getImageWritersByFormatName(CommonUtil.getFileFormat(format)).next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()){
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.5f);
            }

            writer.write(null, new IIOImage(image, null, null), param);
            out.close();
            ios.close();
            writer.dispose();
            return "compressed_" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Async
    public void indexAds(ClassifiedAdvertising classifiedAdvertising){

        UserProfile author = userProfileService.findByUserId(classifiedAdvertising.getAuthorId())
                .orElseThrow(() -> new ApplicationException(APIStatus.ERR_USER_PROFILE_NOT_FOUND));

        AddressResponse addressResponse;

        if(classifiedAdvertising.getLocationType() == Constant.LocationType.DATA_ADDRESS.getValue()){
            addressResponse = addressAggregation.getAddressByLocationId(
                    classifiedAdvertising.getProvinceId(),
                    classifiedAdvertising.getDistrictId(),
                    classifiedAdvertising.getWardId()
            );
        }
        else{
            addressResponse = addressAggregation.getAddressByAddressIdAndUserId(
                            classifiedAdvertising.getAddressId(),
                            classifiedAdvertising.getAuthorId()
                    );
        }

        ClassifiedAdvertisingElastic elasticModal = new ClassifiedAdvertisingElastic();

        elasticModal.setId(classifiedAdvertising.getId());
        elasticModal.setAuthor(author);
        elasticModal.setLocationType(classifiedAdvertising.getLocationType());
        elasticModal.setAddress(addressResponse);
        elasticModal.setImages(classifiedAdvertising.getImages());
        elasticModal.setAdditionalInfo(classifiedAdvertising.getAdditionalInfo());
        elasticModal.setBreadcrumbs(classifiedAdvertising.getBreadcrumbs());
        elasticModal.setMetadata(classifiedAdvertising.getMetadata());
        elasticModal.setCreatedDate(classifiedAdvertising.getCreatedDate());
        elasticModal.setModifiedDate(classifiedAdvertising.getModifiedDate());
        elasticModal.setStatus(classifiedAdvertising.getStatus());

        classifiedAdvertisingElasticService.index(elasticModal);

    }

}
