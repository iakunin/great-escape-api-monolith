package com.greatescape.api.monolith.service.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.SenderServiceConfiguration;
import ru.dezhik.sms.sender.SenderServiceConfigurationBuilder;

@Configuration
public class Config {

    @Bean
    SenderServiceConfiguration serviceConfiguration(
        @Value("${app.integration.sms-ru.api-id}") String apiId
    ) {
        return SenderServiceConfigurationBuilder.create()
            .setApiId(apiId)
            .setTestSendingEnabled(true)
            .setReturnPlainResponse(true)
            .build();
    }

    @Bean
    @Autowired
    SenderService senderService(SenderServiceConfiguration serviceConfiguration) {
        return new SenderService(serviceConfiguration);
    }
}
