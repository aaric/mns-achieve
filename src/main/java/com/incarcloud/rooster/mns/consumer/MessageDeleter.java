package com.incarcloud.rooster.mns.consumer;

import com.aliyun.mns.client.CloudQueue;
import com.incarcloud.rooster.mns.config.QuartzConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * MessageDeleter
 *
 * @author Aaric, created on 2017-06-22T11:06.
 * @since 1.0-SNAPSHOT
 */
public class MessageDeleter implements Runnable {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    private CloudQueue cloudQueue;
    private BlockingQueue<List<String>> blockingQueue;

    public MessageDeleter(CloudQueue cloudQueue, BlockingQueue<List<String>> blockingQueue) {
        this.cloudQueue = cloudQueue;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            List<String> receiptHandles;
            do {
                receiptHandles = blockingQueue.take();
                cloudQueue.batchDeleteMessage(receiptHandles);
            } while (null != receiptHandles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
