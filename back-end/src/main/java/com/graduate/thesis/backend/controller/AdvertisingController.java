package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.entity.Address;
import com.graduate.thesis.backend.entity.Category;
import com.graduate.thesis.backend.entity.ClassifiedAdvertising;
import com.graduate.thesis.backend.entity.UserProfile;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.request.advertising.NewAdvertisingRequest;
import com.graduate.thesis.backend.model.response.AddressResponse;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.repository.aggregation.AddressAggregation;
import com.graduate.thesis.backend.repository.aggregation.LocationAggregation;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.*;
import com.graduate.thesis.backend.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    @PostMapping()
    public ResponseEntity<RestAPIResponse> createNewAdvertising (
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody NewAdvertisingRequest reqModel
    ) {

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
        classifiedAdvertising.setStatus(Constant.Status.ACTIVE.getValue());

        return responseUtil.successResponse(classifiedAdvertisingService.save(classifiedAdvertising));
    }

    @GetMapping(value = Constant.WITHIN_ID)
    public ResponseEntity<RestAPIResponse> getClassifiedAdsDetail (
            @PathVariable("id") String adsId
    ) {

        ClassifiedAdvertising classifiedAdvertising = classifiedAdvertisingService
                .getClassifiedAdsDetail(adsId, Constant.Status.ACTIVE.getValue());

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

}
