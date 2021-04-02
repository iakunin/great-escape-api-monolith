package com.greatescape.api.monolith.config.feign;

import feign.Retryer;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
@EnableFeignClients({"com.greatescape.api.monolith"})
@RequiredArgsConstructor
public class FeignConfiguration {

    private final ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    @Bean
    public Decoder feignDecoder() {
        final var converters = this.messageConverters.getObject().getConverters();

        converters
            .stream()
            .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
            .forEach(converter -> {
                final var types = new ArrayList<>(converter.getSupportedMediaTypes());
                types.add(new MediaType("text", "json"));
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(types);
            });

        return new OptionalDecoder(
            new ResponseEntityDecoder(
                new SpringDecoder(
                    () -> new HttpMessageConverters(converters)
                )
            )
        );
    }
}
