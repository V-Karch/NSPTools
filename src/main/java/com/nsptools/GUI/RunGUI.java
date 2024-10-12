package com.nsptools.GUI;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.application.Application;

public class RunGUI extends Application {
    public static void runGUI(String args[]) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new GridPane(), 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}
