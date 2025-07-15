package com.kims.coreevent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ShutdownHook {

    private static final Logger log = LoggerFactory.getLogger(ShutdownHook.class);

    @PreDestroy
    public void shutdown() {
        log.error("Shutdown hook triggered. Performing cleanup tasks...");
    }
}
