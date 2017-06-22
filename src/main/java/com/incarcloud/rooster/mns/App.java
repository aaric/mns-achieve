package com.incarcloud.rooster.mns;

import com.incarcloud.rooster.mns.config.QuartzConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Spring Boot launcher.
 *
 * @author Aaric, created on 2017-06-20T12:30.
 * @since 1.0-SNAPSHOT
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    /**
     * blockingQueue
     */
    public static final BlockingQueue<List<String>> blockingQueue = new ArrayBlockingQueue<>(50);

    /**
     * Run
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("start...");
    }

    /**
     * Main
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
