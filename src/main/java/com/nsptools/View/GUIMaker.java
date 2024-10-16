package com.nsptools.View;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class GUIMaker {
    public static GridPane mainFrame() {
        GridPane mainFrame = new GridPane();

        // Set up background
        Background background = new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, null));
        mainFrame.setBackground(background);
        
        // Return the main gridpane frame
        return mainFrame;
    }
}
