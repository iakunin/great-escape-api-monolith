package com.greatescape.api.monolith.config.feign;

import feign.Retryer;
import java.util.List;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableFeignClients({"com.greatescape.api.monolith"})
public class FeignConfiguration {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    @Bean
    public HttpMessageConverter<Object> textJsonMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(new MediaType("text", "json")));

        return converter;
    }
}
