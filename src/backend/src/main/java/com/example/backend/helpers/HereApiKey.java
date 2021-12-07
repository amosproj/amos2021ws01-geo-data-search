package com.example.backend.helpers;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Reads in the key for the HERE-API that is in plain text in the <strong>"here-api-key.txt"</strong> file and
 * that was <strong>manually</strong> inserted into the correct folder <strong>"secrets"</strong> by the user.
 */
public class HereApiKey {

    private static final String SEP = File.separator;
    public static final String HERE_API_KEY_FILE_NAME = SEP + "run" + SEP + "secrets" + SEP + "here-api-key";
    public static final String CLASS_PREFIX = "HERE API KEY";
    public static final String UNKNOWN_API_KEY = "UNKNOWN_API_KEY";
    private static final BackendLogger logger = new BackendLogger();
    private static String hereApiKey = UNKNOWN_API_KEY;

    /**
     * This method will not check, if the key is correct. It will only check, if the correct file is in the correct
     * directory and then try to read that file and return its content as a <code>String</code>.
     * <br>
     * In case, this was not successful, it will return <code>"UNKNOWN_API_KEY"</code> as a <code>String</code> and
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
        File hereApiKeyFile = new File(HERE_API_KEY_FILE_NAME);
        try {
            if (hereApiKeyFile.exists() && !hereApiKeyFile.isDirectory()) {
                FileInputStream inputStream = new FileInputStream(HERE_API_KEY_FILE_NAME);
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);
                hereApiKey = reader.readLine();
            } else {
                handleErrorWithFile();
            }
        } catch (NullPointerException | IOException e) {
            logger.error(CLASS_PREFIX, e.getMessage());
            handleErrorWithFile();
        }
    }

    private static void handleErrorWithFile() {
        logger.error(CLASS_PREFIX, "Could not find \"" + HERE_API_KEY_FILE_NAME + "\"!");
        logger.error(CLASS_PREFIX, "Make sure that the file is in the correct directory: secrets" + SEP + "here-api-key.txt");
        hereApiKey = UNKNOWN_API_KEY;
    }
}