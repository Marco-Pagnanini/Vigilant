package io.vigilant;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VigilantConfig.class)
public class VigilantAutoConfiguration {

    @Bean
    public VigilantAppender vigilantAppender(VigilantConfig config) {
        VigilantAppender appender = new VigilantAppender();
        appender.setServerUrl(config.getServerUrl());
        appender.setProjectId(config.getProjectId());
        appender.setApiKey(config.getApiKey());
        appender.start();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(appender);

        return appender;
    }
}
