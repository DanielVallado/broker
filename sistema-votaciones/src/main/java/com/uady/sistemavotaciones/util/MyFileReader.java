package com.uady.sistemavotaciones.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MyFileReader {

    public static List<String> readFile(String filePath) {
        List<String> linesFile = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            linesFile.addAll(lines);
        } catch (IOException ex) {
            log.error("Error reading file");
            return null;
        }

        return linesFile;
    }

}
