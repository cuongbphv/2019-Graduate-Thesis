package com.graduate.thesis.backend.processor.upload;

import com.graduate.thesis.backend.processor.mesage.UploadProcessMessage;

/**
 * @author cuongbphv created on 09/05/2019
 */
public abstract class BaseUploadProcess {

    public abstract BaseUploadProcess setMessageQueue(UploadProcessMessage message);

    public abstract void doProcess();

    public enum UploadType {
        UPLOAD_TEMP_IMAGE
    }

}
