package com.greatescape.api.monolith.config.feign;

import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
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
    public Encoder feignEncoder() {
        return new SpringFormEncoder(new SpringEncoder(this.messageConverters));
    }

    @Bean
    public Decoder feignDecoder() {
        return new OptionalDecoder(
            new ResponseEntityDecoder(
                new SpringDecoder(
                    () -> new HttpMessageConverters(
                        this.prepareConverters(
                            this.messageConverters.getObject().getConverters()
                        )
                    )
                )
            )
        );
    }

    private List<HttpMessageConverter<?>> prepareConverters(
        List<HttpMessageConverter<?>> converters
    ) {
        converters
            .stream()
            .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
            .forEach(converter -> {
                final var types = new ArrayList<>(converter.getSupportedMediaTypes());
                types.add(new MediaType("text", "json"));
                types.add(new MediaType("text", "html"));
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(types);
            });

        return converters;
    }
}
