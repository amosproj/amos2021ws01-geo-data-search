package com.example.backend.helpers;

import com.example.backend.BackendApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HereApiKey {

    public static final String HERE_API_KEY_FILE_NAME = "here-api-key.txt";

    public static String getKey() {
        String line = "UNKNOWN_API_KEY";
        ClassLoader classLoader = BackendApplication.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(HERE_API_KEY_FILE_NAME);
        try {
            assert inputStream != null;
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}