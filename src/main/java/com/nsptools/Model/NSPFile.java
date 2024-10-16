package com.nsptools.Model;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Represents a file used in NSP operations.
 * Provides basic functionalities to validate the file,
 * retrieve its size in megabytes, and access the file object.
 *
 * @author V-Karch
 */
public class NSPFile {
    private final String filename;
    private final File file;

    /**
     * Constructs an NSPFile object with the specified filename.
     * Verifies that the file exists and is not a directory.
     *
     * @param filename the name of the file to be wrapped by NSPFile
     * @throws FileNotFoundException if the specified file does not exist or is a directory
     * @author V-Karch
     */
    public NSPFile(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.file = new File(filename);

        if (!validateFile(this.file)) {
            throw new FileNotFoundException("File " + filename + " could not be found.");
        }
    }

    /**
     * Returns the size of the file in megabytes.
     *
     * @return the size of the file in megabytes (MB)
     * @author V-Karch
     */
    public double getSizeMB() {
        return this.file.length() / (1024 * 1024);
    }

    /**
     * Retrieves the File object representing the NSP file.
     *
     * @return the File object of the NSP file
     * @author V-Karch
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Returns the filename as a string representation of the NSPFile.
     *
     * @return the filename of the NSPFile
     * @author V-Karch
     */
    @Override
    public String toString() {
        return this.filename;
    }

    /**
     * Validates that the given file exists and is not a directory.
     *
     * @param file the File object to validate
     * @return true if the file exists and is not a directory, false otherwise
     * @author V-Karch
     */
    public static boolean validateFile(File file) {
        return file.exists() && !file.isDirectory();
    }
}
