package com.greatescape.api.monolith.config.feign;

import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.greatescape.api.monolith"})
public class FeignConfiguration {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }
}
