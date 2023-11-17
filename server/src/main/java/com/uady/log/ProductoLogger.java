package com.uady.log;

import com.uady.util.MyFileHandler;

import java.util.List;

public class ProductoLogger {

    private static final String LOGS_FILE_PATH = "C:\\Users\\danie\\OneDrive - Universidad Autonoma de Yucatan\\LIS\\LIS - Quinto Semestre\\Aquitectura de Software\\ADA 9 - Broker\\server\\src\\main\\java\\com\\uady\\log\\logs.txt";

    public static void log(String message) {
        MyFileHandler.writeFile(LOGS_FILE_PATH, message);
    }

    public static List<String> getLogs() {
        return MyFileHandler.readFile(LOGS_FILE_PATH);
    }

}
