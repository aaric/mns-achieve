package com.incarcloud.rooster.mns.config;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.common.http.ClientConfiguration;
import com.incarcloud.rooster.mns.App;
import com.incarcloud.rooster.mns.consumer.MessageDeleter;
import com.incarcloud.rooster.mns.consumer.MessageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Scheduled(cron = "${rooster.cronExpress}")
    public void doClearTask() {
        try {
            MessageReader messageReader = new MessageReader(getCloudQueue(), App.blockingQueue);
            MessageDeleter messageDeleter = new MessageDeleter(getCloudQueue(), App.blockingQueue);

            new Thread(messageReader).start();
            new Thread(messageDeleter).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
