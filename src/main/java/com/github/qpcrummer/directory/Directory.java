package com.github.qpcrummer.directory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Directory {
    public static final Path savePath = Paths.get("saves");
    public static void createDirectories() {
        try {

            if (Files.notExists(savePath)) {
                Files.createDirectory(savePath);
            }
        } catch(IOException ignored) {
        }
    }
}
