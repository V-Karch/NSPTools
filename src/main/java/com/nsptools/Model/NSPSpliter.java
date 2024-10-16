package com.nsptools.Model;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

/**
 * Handles the splitting of NSP files into smaller parts.
 * This class creates split files in a separate output directory.
 *
 * @author V-Karch
 */
public class NSPSpliter {
    private final NSPFile nspfile;
    private final File outputDir;

    /**
     * Constructs an NSPSpliter object with the specified filename.
     * Verifies that the file meets the minimum size requirement and prepares the output directory.
     *
     * @param filename the name of the NSP file to be split
     * @throws FileNotFoundException if the specified file does not exist or is a directory
     * @throws IllegalArgumentException if the file size is less than the required size of 4000 MB
     */
    public NSPSpliter(String filename) throws FileNotFoundException, IllegalArgumentException {
        this.nspfile = new NSPFile(filename);

        if (!validateFileSize(nspfile)) {
            throw new IllegalArgumentException("File " + nspfile.toString()
                    + " was smaller than the required size of 4000 MB. Actual Size: " + nspfile.getSizeMB() + " MB.");
        }

        // Create the output directory
        this.outputDir = new File(nspfile.getFile().getParent(), "split_output");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
    }

    /**
     * Generates a new filename for each part of the split file.
     *
     * @param partNumber the part number to append to the filename
     * @return the full path of the new file part
     */
    private String getNewFileName(int partNumber) {
        String fileName = this.nspfile.getFile().getName();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
        return new File(outputDir, baseName + "_part_" + partNumber + extension).getPath();
    }

    /**
     * Splits the NSP file into smaller parts, each with a maximum size of 4000 MB.
     * The split files are written to the output directory.
     */
    public void split() {
        long partSize = 4000L * 1024 * 1024; // 4 gigabytes in bytes
        byte[] buffer = new byte[4 * 1024 * 1024]; // 4 MB buffer size
        int partNumber = 1;

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(this.nspfile.getFile()))) {
            int bytesRead;
            BufferedOutputStream output = null;
            long bytesWrittenToPart = 0;

            while ((bytesRead = input.read(buffer)) > 0) {
                // If no file is open or we've reached the part size limit, create a new file part
                if (output == null || bytesWrittenToPart >= partSize) {
                    if (output != null) {
                        output.close(); // Close the previous part file
                    }
                    String newFileName = getNewFileName(partNumber++);
                    output = new BufferedOutputStream(new FileOutputStream(newFileName));
                    bytesWrittenToPart = 0;
                }

                // Write the data to the current file part
                output.write(buffer, 0, bytesRead);
                bytesWrittenToPart += bytesRead;
            }

            if (output != null) {
                output.close(); // Ensure the last file part is properly closed
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates that the NSP file is larger than 4000 MB.
     *
     * @param nspfile the NSP file to validate
     * @return true if the file size is greater than 4000 MB, false otherwise
     */
    public static boolean validateFileSize(NSPFile nspfile) {
        return nspfile.getSizeMB() > 4000; // Ensure that the file is larger than 4GB
    }
}
