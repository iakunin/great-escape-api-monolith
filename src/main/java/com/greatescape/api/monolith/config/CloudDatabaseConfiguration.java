package com.greatescape.api.monolith.config;

import io.github.jhipster.config.JHipsterConstants;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_CLOUD)
@Slf4j
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private static final String CLOUD_CONFIGURATION_HIKARI_PREFIX = "spring.datasource.hikari";

    @Bean
    @ConfigurationProperties(CLOUD_CONFIGURATION_HIKARI_PREFIX)
    public DataSource dataSource() {
        log.info("Configuring JDBC datasource from a cloud provider");
        return connectionFactory().dataSource();
    }
}
