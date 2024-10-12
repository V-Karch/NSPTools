package com.nsptools.Model;

import java.io.File;
import java.io.FileNotFoundException;

public class NSPFile {
    private final String filename;
    private final File file;

    public NSPFile(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.file = new File(filename);

        if (!validateFile(this.file)) {
            throw new FileNotFoundException("File " + filename + " could not be found.");
        }

    }

    public double getSizeMB() {
        return this.file.length() / (1024 * 1024);
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return this.filename;
    }

    public static boolean validateFile(File file) {
        return file.exists() && !file.isDirectory();
    }
}
