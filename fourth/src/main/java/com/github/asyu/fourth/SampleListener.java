package com.github.asyu.fourth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleListener implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(SampleListener.class);

    @Autowired
    private AsProperties properties;

    @Autowired
    private String hello;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("=====================");
        logger.debug(properties.getName());
        logger.debug(hello);
        logger.debug("=====================");
    }
}