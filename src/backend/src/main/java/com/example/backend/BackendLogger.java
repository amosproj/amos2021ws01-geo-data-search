package com.example.backend;

public class BackendLogger {

    public BackendLogger() {

    }

    public void info(String log_msg) {
        log(" [INFO] " + log_msg);
    }

    public void error(String log_msg) {
        log("[ERROR] " + log_msg);
    }

    private void log(String log_msg) {
        System.out.println(log_msg);
    }
}