package com.greatescape.api.monolith.utils.wiremock;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.common.net.HttpHeaders;
import com.greatescape.api.monolith.utils.ResourceOf;
import java.io.IOException;

public final class JsonResponse extends ResponseDefinitionBuilder {
    public JsonResponse(final String path) throws IOException {
        super();
        this.withStatus(200);
        this.withHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        this.withBody(new ResourceOf(path).asString());
    }
}
