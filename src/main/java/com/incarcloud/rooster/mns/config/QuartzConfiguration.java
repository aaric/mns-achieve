package com.incarcloud.rooster.mns.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.common.http.ClientConfiguration;
import com.aliyun.mns.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * QuartzConfiguration
 *
 * @author Aaric, created on 2017-06-21T13:49.
 * @since 1.0-SNAPSHOT
 */
@Configuration
@EnableScheduling
public class QuartzConfiguration {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    @Value("${aliyun.accessKeyId}")
    private String accessId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKey;

    @Value("${aliyun.MNS.endpoint}")
    private String accountEndpoint;

    //@Value("${aliyun.MNS.maxConnections}")
    //private int maxConnections;

    //@Value("${aliyun.MNS.maxConnectionsPerRoute}")
    //private int maxConnectionsPerRoute;

    @Value("${aliyun.MNS.queueName}")
    private String queueName;

    @Value("${aliyun.MNS.topicName}")
    private String topicName;

    /**
     * getCloudQueue
     *
     * @return
     */
    public CloudQueue getCloudQueue() {
        ClientConfiguration config = new ClientConfiguration();
        //config.setMaxConnections(maxConnections);
        //config.setMaxConnectionsPerRoute(maxConnectionsPerRoute);
        CloudAccount account = new CloudAccount(accessId, accessKey, accountEndpoint, config);
        return account.getMNSClient().getQueueRef(queueName);
    }

    /**
     * Counter
     */
    private static long counter = 0;

    @Scheduled(cron = "${rooster.cronExpress}")
    public void doClearTask() {
        try {
            List<String> receiptHandles;
            List<Message> batchPopMessage;
            CloudQueue cloudQueue = getCloudQueue();
            for (int i = 0; i < 1000; i++) {
                batchPopMessage = cloudQueue.batchPopMessage(16, 30);
                if(null != batchPopMessage && 0 < batchPopMessage.size()) {
                    receiptHandles = new ArrayList<>();
                    for (Message message: batchPopMessage) {
                        logger.info("{}: [{}] {}", MessageFormat.format("{0,number,00000000000000000000}", ++counter), queueName, message.getMessageId());
                        receiptHandles.add(message.getReceiptHandle());
                    }
                    cloudQueue.batchDeleteMessage(receiptHandles);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
