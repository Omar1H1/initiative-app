package com.Initiative.app.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextTaskExecutor;

@Configuration
public class WebSocketSecurityConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor delegateExecutor = new ThreadPoolTaskExecutor();
        delegateExecutor.setCorePoolSize(10);
        delegateExecutor.setMaxPoolSize(20);
        delegateExecutor.initialize();

        return new DelegatingSecurityContextTaskExecutor(delegateExecutor);
    }
}
