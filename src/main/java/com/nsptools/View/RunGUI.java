package com.nsptools.View;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;

public class RunGUI extends Application {
    public static void runGUI(String args[]) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane mainFrame = GUIMaker.mainFrame(stage);

        Scene scene = new Scene(mainFrame, 750, 170);
        stage.setScene(scene);
        stage.setTitle("NSP Tools");
        stage.show();
    }
}
