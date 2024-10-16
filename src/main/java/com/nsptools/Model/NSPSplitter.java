package com.nsptools.Model;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

/**
 * Handles the splitting of NSP files into multiple parts.
 * This class supports progress updates via a listener for both console and GUI integration.
 * 
 * @author V-Karch
 */
public class NSPSplitter {
    private final NSPFile nspfile;
    private final File outputDir;
    private final ProgressListener progressListener;

    /**
     * Constructs an NSPSplitter object with the specified NSP file and progress listener.
     * 
     * @param filename         the name of the NSP file to be split
     * @param progressListener a listener for progress updates
     * @throws FileNotFoundException    if the NSP file is not found
     * @throws IllegalArgumentException if the file size is smaller than the required minimum
     */
    public NSPSplitter(String filename, ProgressListener progressListener) throws FileNotFoundException, IllegalArgumentException {
        this.nspfile = new NSPFile(filename);
        this.progressListener = progressListener;

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

    /**
     * Splits the NSP file into multiple parts and updates progress through the listener.
     */
    public void split() {
        long partSize = 4000L * 1024 * 1024; // 4 GB in bytes
        long totalSize = nspfile.getFile().length();
        byte[] buffer = new byte[4 * 1024 * 1024]; // 4 MB buffer size
        int partNumber = 1;
        long totalBytesRead = 0;

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
                totalBytesRead += bytesRead;

                // Update progress
                if (progressListener != null) {
                    double progress = (double) totalBytesRead / totalSize;
                    progressListener.onProgressUpdate(progress);
                }
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
