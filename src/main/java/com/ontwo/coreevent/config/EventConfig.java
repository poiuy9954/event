package com.ontwo.coreevent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class EventConfig {

    private final ThreadInfoProperties threadInfoProperties;

    public EventConfig(ThreadInfoProperties threadInfoProperties) {
        this.threadInfoProperties = threadInfoProperties;
    }

    @Bean(name = "eventListenerExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadInfoProperties.getCoreSize());
        executor.setMaxPoolSize(threadInfoProperties.getMaxSize());
        executor.setQueueCapacity(threadInfoProperties.getQueueCapacity());
        executor.setThreadNamePrefix("event-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
