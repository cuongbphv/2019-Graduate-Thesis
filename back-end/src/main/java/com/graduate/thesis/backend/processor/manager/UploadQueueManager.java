package com.graduate.thesis.backend.processor.manager;

import com.graduate.thesis.backend.processor.mesage.UploadProcessMessage;

import java.util.LinkedList;

/**
 * @author cuongbphv created on 09/05/2019
 */
public class UploadQueueManager {

    private static UploadQueueManager instance;

    public static synchronized UploadQueueManager getInstance() {
        if (instance == null) {
            instance = new UploadQueueManager();
        }
        return instance;
    }

    // Queue management
    private LinkedList<UploadProcessMessage> queues;

    private UploadQueueManager() {
        queues = new LinkedList<>();
    }

    // get message queue
    public synchronized UploadProcessMessage getQueueItem() {
        if (!queues.isEmpty()) {
            UploadProcessMessage item = queues.getFirst();
            if (item != null) {
                queues.remove(item);
            }
            return item;
        } else {
            return null;
        }
    }

    // Add message queue
    public synchronized void addQueueItem(UploadProcessMessage obj) {
        queues.addLast(obj);
        System.out.println("Added new UploadProcessMessage");
    }

}
