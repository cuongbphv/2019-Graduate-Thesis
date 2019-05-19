package com.graduate.thesis.backend.processor.mesage;

import com.graduate.thesis.backend.processor.upload.BaseUploadProcess.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cuongbphv created on 09/05/2019
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadProcessMessage {

    private String queueId;
    private MultipartFile uploadFile;
    private UploadType type;
    private String userId;

}
