package com.nsptools.View;

import java.io.File;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.Alert.AlertType;


public class GUIMaker {
    public static GridPane mainFrame(Stage primaryStage) {
        GridPane mainFrame = new GridPane();

        // Set up background
        Background background = new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, null));
        mainFrame.setBackground(background);

        // Add selection button for selecting an NSP or XCI File
        mainFrame.add(selectNSPFileButton(primaryStage), 0, 0);

        // Return the main gridpane frame
        return mainFrame;
    }

    public static Button selectNSPFileButton(Stage primaryStage) {
        Button selectNSPButton = new Button("Select NSP or XCI File for splitting");

        // Set action for the button
        selectNSPButton.setOnAction(event -> {
            FileChooser fileChooser = selectNSPChooser();
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                // Do something with the selected file
                // For demonstration, we can show an alert with the file path
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("File Selected");
                alert.setHeaderText(null);
                alert.setContentText("Selected file: " + selectedFile.getAbsolutePath());
                alert.showAndWait();
            }
        });

        return selectNSPButton;
    }

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
}
