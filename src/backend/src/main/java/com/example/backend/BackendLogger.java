package com.example.backend;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class BackendLogger {

    private static final String prefixInfo = "INFO";
    private static final String prefixError = "ERROR";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public BackendLogger() {

    }

    /**
     * Will log an info message in a new line in this format:<br>
     * <code>yyyy.MM.dd HH:mm:ss [INFO] [CLASS_NAME] Message</code>
     *
     * @param classPrefix the name of the logging class
     * @param logMsg      the message to be logged
     */
    public void info(String classPrefix, String logMsg) {
        log(prefixInfo, classPrefix, logMsg);
    }

    /**
     * Will log an error message in a new line in this format:<br>
     * <code>yyyy.MM.dd HH:mm:ss [ERROR] [CLASS_NAME] Message</code>
     *
     * @param classPrefix the name of the logging class
     * @param logMsg      the message to be logged
     */
    public void error(String classPrefix, String logMsg) {
        log(prefixError, classPrefix, logMsg);
    }

    private void log(String logTypePrefix, String classPrefix, String logMsg) {
        // TODO Replace or expand this with real logging into a file
        System.out.println(simpleDateFormat.format(new Timestamp(System.currentTimeMillis())) + " [" + logTypePrefix + "] [" + classPrefix + "] " + logMsg);
    }
}