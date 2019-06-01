package com.graduate.thesis.backend.processor.upload;

import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.processor.mesage.UploadProcessMessage;
import com.graduate.thesis.backend.security.TokenProvider;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.CommonUtil;
import com.graduate.thesis.backend.util.FileUtil;
import com.graduate.thesis.backend.util.UniqueID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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

/**
 * @author cuongbphv created on 09/05/2019
 */
public class UploadTempImageProcessor {// extends BaseUploadProcess implements Cloneable {

//    @Value("${file.upload-dir}")
//    private String uploadPath;
//
//    @Autowired
//    FileStorageService fileStorageService;
//
//    private UploadProcessMessage queueMessage;
//
//    @Override
//    public BaseUploadProcess setMessageQueue(UploadProcessMessage message) {
//        this.queueMessage = message;
//        return this;
//    }
//
//
//    @Override
//    public void doProcess() {
//        try {
//            processUploadFile(this.queueMessage);
//        } catch (Exception e) {
//            System.out.println("Error when doing publish kinoDen: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
//
//    private void processUploadFile(UploadProcessMessage message) {
//        String ext = CommonUtil.getFileExtension();
//        String fileName = "temp_" + userPrincipal.getId() + ext;
//        String url = fileStorageService.storeFile(tempImage, fileName);
//
//        // create logo watermark
//        String urlWatermark = makeWaterMark(userPrincipal.getId(), url, ext);
//        if (urlWatermark.isEmpty()) {
//            throw new ApplicationException(APIStatus.ERR_UPLOAD_FILE);
//        }
//        FileUtil.deleteFile(url);
//
//        // compressed image
//        String resUrl = compressImage(urlWatermark, queueMessage, ext);
//        if (resUrl.isEmpty()) {
//            throw new ApplicationException(APIStatus.ERR_UPLOAD_FILE);
//        }
//        FileUtil.deleteFile(urlWatermark);
//
//        // create access link
//        String accessLink = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/api/v1/files/")
//                .path(resUrl)
//                .toUriString();
//    }
//
//    private String makeWaterMark(String userId, String src, String format) {
//        try {
//            // Read image uploaded
//            BufferedImage srcImage = ImageIO.read(new File(src));
//            int width = srcImage.getWidth(null);
//            int height = srcImage.getHeight(null);
//
//            if (width < 500 || height < 750) {
//                throw new ApplicationException(APIStatus.ERR_SMALL_IMAGE);
//            }
//
//            // Draw Watermark
//            BufferedImage destImage = new BufferedImage(width, height,
//                    BufferedImage.TYPE_INT_RGB);
//            Graphics g = destImage.getGraphics();
//            g.drawImage(srcImage, 0, 0, width, height, null);
//            g.setColor(Color.WHITE);
//            g.setFont(new Font("Arial", Font.BOLD, 24));
//            g.drawString("SmartMarket", width - 150, height - 10);
//            g.dispose();
//
//            // Write new image
//            String fileName = UniqueID.getUUID() + userId + format;
//            String url = uploadPath + fileName;
//            File f = new File(url);
//            if (f.getParentFile() == null) {
//                f.getParentFile().mkdirs();
//            }
//            ImageIO.write(destImage, CommonUtil.getFileFormat(format), f);
//            return url;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "";
//        }
//    }
//
//    private String compressImage(String inputFile, String userId, String format) {
//        try {
//            BufferedImage image = ImageIO.read(new File(inputFile));
//
//            String fileName = UniqueID.getUUID() + userId + format;
//            String url = uploadPath + "compressed_" + fileName;
//
//            File output = new File(url);
//            OutputStream out = new FileOutputStream(output);
//
//            ImageWriter writer =  ImageIO.getImageWritersByFormatName(CommonUtil.getFileFormat(format)).next();
//            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
//            writer.setOutput(ios);
//
//            ImageWriteParam param = writer.getDefaultWriteParam();
//            if (param.canWriteCompressed()){
//                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//                param.setCompressionQuality(0.5f);
//            }
//
//            writer.write(null, new IIOImage(image, null, null), param);
//            out.close();
//            ios.close();
//            writer.dispose();
//            return "compressed_" + fileName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
}
