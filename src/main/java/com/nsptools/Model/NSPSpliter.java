package com.nsptools.Model;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

public class NSPSpliter {
    private final NSPFile nspfile;
    private final File outputDir;

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

    private String getNewFileName(int partNumber) {
        String fileName = this.nspfile.getFile().getName();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
        return new File(outputDir, baseName + "_part_" + partNumber + extension).getPath();
    }

    public void split() {
        long partSize = 4000L * 1024 * 1024; // 3.9 gigabytes in bytes
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

    public static boolean validateFileSize(NSPFile nspfile) {
        return nspfile.getSizeMB() > 4000; // Ensure that the file is larger than 4GB
    }
}
