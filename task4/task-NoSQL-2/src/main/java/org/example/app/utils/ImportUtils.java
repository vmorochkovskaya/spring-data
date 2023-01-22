package org.example.app.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImportUtils {

    public static String getStringFromResource(String resource) throws IOException {
        Resource input = new ClassPathResource(resource);
        return Files.readString(Path.of(input.getURI()));
    }
}
