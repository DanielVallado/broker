package com.uady.util;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MyFileHandler {

    public static List<String> readFile(String filePath) {
        List<String> linesFile = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            linesFile.addAll(lines);
        } catch (IOException ex) {
            log.error("Error reading file {}: {}", filePath,ex.getMessage());
            return null;
        }

        return linesFile;
    }

    public static void writeFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException ex) {
            log.error("Error writing file: {}", ex.getMessage());
        }
    }

}
