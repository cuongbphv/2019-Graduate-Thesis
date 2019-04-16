package com.graduate.thesis.backend.controller;

import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.service.FileStorageService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.FILE)
public class FileController {

    @Autowired
    FileStorageService fileStorageService;

    @RequestMapping( value= Constant.WITHIN_ID, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("id") String fileName,
            HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ApplicationException(APIStatus.ERR_FILE_TYPE);
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }
}
