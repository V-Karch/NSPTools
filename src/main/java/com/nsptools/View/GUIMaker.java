package com.nsptools.View;

import java.io.File;
import java.io.FileNotFoundException;

import com.nsptools.Model.NSPSplitter;

import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.Alert.AlertType;


/**
 * The GUIMaker class is responsible for creating the GUI components
 * for the NSP/XCI file splitting and combining tool.
 * 
 * @author V-Karch
 */
public class GUIMaker {
    private static Label filePathLabel;     // Label to show the selected file path
    private static Label directoryPathLabel; // Label to show the selected directory path

    /**
     * Creates the main frame of the GUI.
     * 
     * @param primaryStage the primary stage for this application
     * @return a GridPane containing the main interface elements
     * @author V-Karch
     */
    public static GridPane mainFrame(Stage primaryStage) {
        GridPane mainFrame = new GridPane();
        mainFrame.setHgap(10);

        // Set up background
        Background background = new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, null));
        mainFrame.setBackground(background);

        // Add selection button for selecting an NSP or XCI File
        mainFrame.add(selectNSPFileButton(primaryStage), 0, 0);

        // Initialize the label to show the selected file path
        filePathLabel = new Label("No file selected.");
        mainFrame.add(filePathLabel, 1, 0); // Add the label to the grid

        // Add selection button for selecting a directory
        mainFrame.add(selectDirectoryButton(primaryStage), 0, 1);

        // Initialize the label to show the selected directory path
        directoryPathLabel = new Label("No directory selected.");
        mainFrame.add(directoryPathLabel, 1, 1); // Add the label to the grid

        // Add the split button
        mainFrame.add(splitButton(), 0, 2);

        // Add the combine button
        mainFrame.add(combineButton(), 0, 3);


        // Return the main gridpane frame
        return mainFrame;
    }

    /**
     * Creates a button for selecting an NSP or XCI file.
     * 
     * @param primaryStage the primary stage for this application
     * @return a Button for selecting NSP/XCI files
     * @author V-Karch
     */
    public static Button selectNSPFileButton(Stage primaryStage) {
        Button selectNSPButton = new Button("Select NSP or XCI File for splitting");
        selectNSPButton.setMaxWidth(Double.MAX_VALUE);

        // Set action for the button
        selectNSPButton.setOnAction(event -> {
            FileChooser fileChooser = selectNSPChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                // Update the label with the selected file's path
                filePathLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
            } else {
                filePathLabel.setText("No file selected.");
            }
        });

        return selectNSPButton;
    }

    /**
     * Creates a button for selecting a directory for combining NSP/XCI files.
     * 
     * @param primaryStage the primary stage for this application
     * @return a Button for selecting a directory
     * @author V-Karch
     */
    public static Button selectDirectoryButton(Stage primaryStage) {
        Button selectDirectoryButton = new Button("Select Directory for Combining");
        selectDirectoryButton.setMaxWidth(Double.MAX_VALUE);

        // Set action for the button
        selectDirectoryButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = selectDirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory != null) {
                // Update the label with the selected directory's path
                directoryPathLabel.setText("Selected directory: " + selectedDirectory.getAbsolutePath());
            } else {
                directoryPathLabel.setText("No directory selected.");
            }
        });

        return selectDirectoryButton;
    }

    /**
     * Configures a FileChooser for selecting NSP/XCI files.
     * 
     * @return a FileChooser configured for NSP/XCI files
     * @author V-Karch
     */
    public static FileChooser selectNSPChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select NSP or XCI File for Splitting");
        
        // Add extension filters for NSP and XCI files
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("NSP Files", "*.nsp"),
            new FileChooser.ExtensionFilter("XCI Files", "*.xci"),
            new FileChooser.ExtensionFilter("All Supported Files", "*.nsp", "*.xci")
        );

        return fileChooser;
    }

    /**
     * Configures a DirectoryChooser for selecting a directory.
     * 
     * @return a DirectoryChooser configured for selecting a directory
     * @author V-Karch
     */
    public static DirectoryChooser selectDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory for Combining NSP/XCI Files");
        return directoryChooser;
    }

    /**
     * Creates a button that triggers the split action.
     * 
     * @return a Button configured for splitting an NSP file
     * @author V-Karch
     */
    public static Button splitButton() {
        Button splitButton = new Button("Split");
        splitButton.setMaxWidth(Double.MAX_VALUE);
    
        splitButton.setOnAction(event -> {
            // Check if the file path is set
            if (filePathLabel.getText().equals("No file selected.")) {
                // Create an alert
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("File Not Selected");
                alert.setHeaderText(null); // No header text
                alert.setContentText("Please select a file to split before proceeding.");
    
                // Show the alert and wait for the user to respond
                alert.showAndWait();
            } else {
                try {
                    NSPSplitter nspsplitter = new NSPSplitter(filePathLabel.getText().split(": ")[1], null);
                    nspsplitter.split();
                } catch (FileNotFoundException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
    
        return splitButton;
    }

    public static Button combineButton() {
        Button combineButton = new Button("Combine");
        combineButton.setMaxWidth(Double.MAX_VALUE);
        return combineButton;
    }
}
