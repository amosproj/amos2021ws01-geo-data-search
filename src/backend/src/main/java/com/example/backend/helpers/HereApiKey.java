package com.example.backend.helpers;

import com.example.backend.BackendApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Reads in the key for the HERE-API that is in plain text in the <strong>"here-api-key.txt"</strong> file and
 * that was <strong>manually</strong> inserted into the correct folder
 * <strong>"main\src\backend\src\main\resources"</strong> by the user.
 */
public class HereApiKey {

    public static final String HERE_API_KEY_FILE_NAME = "here-api-key.txt";
    public static final String CLASS_PREFIX = "HERE API KEY";
    public static final String UNKNOWN_API_KEY = "UNKNOWN_API_KEY";
    private static final BackendLogger logger = new BackendLogger();
    private static String hereApiKey = UNKNOWN_API_KEY;

    /**
     * This method will not check, if the key is correct. It will only check, if the correct file is in the correct
     * directory and then try to read that file and return its content as a <code>String</code>.
     * <br>
     * In case, this was not successful, it will return <code>UNKNOWN_API_KEY</code> as a <code>String</code> and
     * log the problem as an error.
     *
     * @return HERE-API-KEY
     */
    public static String getKey() {
        if (hereApiKey.equals(UNKNOWN_API_KEY) || hereApiKey.isEmpty()) {
            retrieveTheKeyFromFile();
        }
        return hereApiKey;
    }

    private static void retrieveTheKeyFromFile() {
        ClassLoader classLoader = BackendApplication.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(HERE_API_KEY_FILE_NAME);
        try {
            assert inputStream != null;
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            hereApiKey = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            hereApiKey = UNKNOWN_API_KEY;
        } catch (NullPointerException e) {
            logger.error(CLASS_PREFIX, "Could not find \"" + HERE_API_KEY_FILE_NAME + "\"!");
            logger.error(CLASS_PREFIX, "Make sure, the file is in the correct directory: \"src\\backend\\src\\main\\resources\"");
            hereApiKey = UNKNOWN_API_KEY;
        }
    }
}