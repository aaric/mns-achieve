package com.incarcloud.rooster.mns.consumer;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.model.Message;
import com.incarcloud.rooster.mns.config.QuartzConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * MessageReader
 *
 * @author Aaric, created on 2017-06-22T11:05.
 * @since 1.0-SNAPSHOT
 */
public class MessageReader implements Runnable {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    /**
     * Counter
     */
    private static long counter = 0;

    private CloudQueue cloudQueue;
    private BlockingQueue<List<String>> blockingQueue;

    public MessageReader(CloudQueue cloudQueue, BlockingQueue<List<String>> blockingQueue) {
        this.cloudQueue = cloudQueue;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        List<Message> batchPopMessage;
        for (int i = 0; i < 1000; i++) {
            try {
                batchPopMessage = cloudQueue.batchPopMessage(16, 30);
                if(null != batchPopMessage && 0 < batchPopMessage.size()) {
                    List<String> receiptHandles = new ArrayList<>();
                    for (Message message: batchPopMessage) {
                        logger.info("{}: {}", MessageFormat.format("{0,number,00000000000000000000}", ++counter), message.getMessageId());
                        receiptHandles.add(message.getReceiptHandle());
                    }
                    blockingQueue.put(receiptHandles);
                } else {
                    logger.info("No message to delete!");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
