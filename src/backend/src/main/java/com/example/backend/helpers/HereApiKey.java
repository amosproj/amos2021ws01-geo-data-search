package com.example.backend.helpers;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HereApiKey {

    public static String getKey() {
        URI uri = Paths.get("src/backend/src/main/resources/here-api-key.txt").toUri();
        Path file = Paths.get(uri);
        System.out.println(file.toFile().getAbsolutePath());
        System.out.println(file.toFile().exists());
        return "INSERT_KEY_HERE_MANUALLY";
    }
}