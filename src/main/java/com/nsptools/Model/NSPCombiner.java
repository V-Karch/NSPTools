package com.nsptools.Model;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 * Handles the combining of NSP file parts into a single file.
 * This class supports progress updates via a listener for both console and GUI integration.
 * 
 * @author V-Karch
 */
public class NSPCombiner {
    private final File inputDir;
    private final List<File> partFiles;
    private final String outputFileName;
    private final ProgressListener progressListener;

    /**
     * Constructs an NSPCombiner object with the specified input directory and progress listener.
     * 
     * @param directoryPath    the path to the directory containing the NSP file parts
     * @param progressListener a listener for progress updates
     * @throws IllegalArgumentException if the directory is invalid or contains no part files
     */
    public NSPCombiner(String directoryPath, ProgressListener progressListener) throws IllegalArgumentException {
        this.inputDir = new File(directoryPath);
        this.progressListener = progressListener;

        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a valid directory: " + directoryPath);
        }

        this.partFiles = findPartFiles();

        if (partFiles.isEmpty()) {
            throw new IllegalArgumentException("No valid part files found in the directory: " + directoryPath);
        }

        this.outputFileName = determineOutputFileName();
    }

    private List<File> findPartFiles() {
        File[] files = inputDir.listFiles();
        List<File> validFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName().toLowerCase();
                if (fileName.matches(".*_part_\\d+\\.(nsp|xci)") || fileName.matches("\\d{2}")) {
                    validFiles.add(file);
                }
            }

            validFiles.sort(Comparator.comparing(this::extractPartNumber));
        }

        return validFiles;
    }

    private int extractPartNumber(File file) {
        String fileName = file.getName().toLowerCase();
        String partNumberStr;

        // Match patterns like "filename_part_x.nsp" or just "00"
        if (fileName.matches(".*_part_\\d+\\.(nsp|xci)")) {
            partNumberStr = fileName.replaceAll(".*_part_(\\d+)\\.(nsp|xci)", "$1");
        } else if (fileName.matches("\\d{2}")) {
            partNumberStr = fileName;
        } else {
            throw new IllegalArgumentException("Unexpected file name format: " + fileName);
        }

        return Integer.parseInt(partNumberStr);
    }

    private String determineOutputFileName() {
        String extension = partFiles.get(0).getName().endsWith(".xci") ? "xci" : "nsp";
        return new File(inputDir, "output." + extension).getPath();
    }

    /**
     * Combines the NSP file parts into a single file and updates progress through the listener.
     */
    public void combine() {
        long totalSize = partFiles.stream().mapToLong(File::length).sum();
        long totalBytesRead = 0;

        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFileName))) {
            for (File partFile : partFiles) {
                try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(partFile))) {
                    byte[] buffer = new byte[4 * 1024 * 1024]; // 4 MB buffer
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) > 0) {
                        output.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;

                        // Update progress
                        if (progressListener != null) {
                            double progress = (double) totalBytesRead / totalSize;
                            progressListener.onProgressUpdate(progress);
                        }
                    }
                }
            }
            System.out.println("Files combined successfully into: " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
