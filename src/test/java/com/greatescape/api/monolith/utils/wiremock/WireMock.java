package com.greatescape.api.monolith.utils.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

public class WireMock {
    public static WireMockServer initWireMockServer() {
        final var server = new WireMockServer(
            WireMockConfiguration.options()
                .dynamicPort()
                .extensions(new ResponseTemplateTransformer(false))
        );
        server.start();

        return server;
    }
}
