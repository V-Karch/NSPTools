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
 * Handles the combination of NSP file parts into a single output file.
 * This class searches for split files in a specified directory and combines them into a complete NSP or XCI file.
 *
 * @author V-Karch
 */
public class NSPCombiner {
    private final File inputDir;
    private final List<File> partFiles;
    private final String outputFileName;

    /**
     * Constructs an NSPCombiner object with the specified directory path.
     * Verifies that the directory contains valid part files to be combined.
     *
     * @param directoryPath the path to the directory containing the split files
     * @throws IllegalArgumentException if the directory is invalid or contains no valid part files
     */
    public NSPCombiner(String directoryPath) throws IllegalArgumentException {
        this.inputDir = new File(directoryPath);

        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a valid directory: " + directoryPath);
        }

        this.partFiles = findPartFiles();

        if (partFiles.isEmpty()) {
            throw new IllegalArgumentException("No valid part files found in the directory: " + directoryPath);
        }

        this.outputFileName = determineOutputFileName();
    }

    /**
     * Finds and sorts all valid part files in the specified directory.
     *
     * @return a list of valid part files sorted in the correct order
     */
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

    /**
     * Extracts the part number from the file name to determine its order.
     *
     * @param file the file from which to extract the part number
     * @return the part number as an integer
     * @throws IllegalArgumentException if the file name format is unexpected
     */
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

    /**
     * Determines the name of the combined output file based on the file extension of the parts.
     *
     * @return the name of the output file with the appropriate extension
     */
    private String determineOutputFileName() {
        String extension = partFiles.get(0).getName().endsWith(".xci") ? "xci" : "nsp";
        return new File(inputDir, "output." + extension).getPath();
    }

    /**
     * Combines all the valid part files into a single output file.
     * The combined file is created in the same directory as the part files.
     */
    public void combine() {
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFileName))) {
            for (File partFile : partFiles) {
                try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(partFile))) {
                    byte[] buffer = new byte[4 * 1024 * 1024]; // 4 MB buffer
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) > 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
            }
            System.out.println("Files combined successfully into: " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
