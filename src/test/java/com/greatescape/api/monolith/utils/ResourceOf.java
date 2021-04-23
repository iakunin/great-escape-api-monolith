package com.greatescape.api.monolith.utils;

import java.io.IOException;
import java.nio.file.Files;
import org.springframework.core.io.ClassPathResource;

public final class ResourceOf {

    private final CharSequence path;

    public ResourceOf(final CharSequence path) {
        this.path = path;
    }

    public String asString() throws IOException {
        return
            new String(
                Files.readAllBytes(
                    new ClassPathResource(this.path.toString()).getFile().toPath()
                )
            );
    }
}
