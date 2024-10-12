package com.nsptools.Model;

import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;

public class NSPSpliter {
    private final NSPFile nspfile;

    public NSPSpliter(String filename) throws FileNotFoundException, IllegalArgumentException {
        this.nspfile = new NSPFile(filename);


        if (!validateFileSize(nspfile)) {
            throw new IllegalArgumentException("File " + nspfile.toString() + " was smaller than the required size of 4000 mb. Actual Size: " + nspfile.getSizeMB() + " mb.");
        }
    }

    public static boolean validateFileSize(NSPFile nspfile) {
        return nspfile.getSizeMB() > 4000;
        // Should ensure that the file is larger than 4GB
    }
}
