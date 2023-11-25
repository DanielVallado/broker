package com.uady.log;

import com.uady.util.MyFileHandler;

import java.util.List;

public class ProductoLogger {

    private static final String LOGS_FILE_PATH = "logs/logs.txt";

    public static void log(String message) {
        MyFileHandler.writeFile(LOGS_FILE_PATH, message);
    }

    public static List<String> getLogs() {
        return MyFileHandler.readFile(LOGS_FILE_PATH);
    }

}
