package com.greatescape.api.monolith.service.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.SenderServiceConfiguration;
import ru.dezhik.sms.sender.SenderServiceConfigurationBuilder;

@Configuration
public class Config {

    @Bean
    @Profile("prod")
    SenderServiceConfiguration serviceConfiguration(
        @Value("${app.integration.sms-ru.api-id}") String apiId
    ) {
        return SenderServiceConfigurationBuilder.create()
            .setApiId(apiId)
            .setReturnPlainResponse(true)
            .build();
    }

    @Bean
    @Profile("prod")
    @Autowired
    SenderService senderService(SenderServiceConfiguration serviceConfiguration) {
        return new SenderService(serviceConfiguration);
    }
}
