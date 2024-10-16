package com.nsptools.View;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class RunGUI extends Application {
    public static void runGUI(String args[]) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(GUIMaker.mainFrame(stage), 750, 500);
        stage.setScene(scene);
        stage.setTitle("NSP Tools");
        stage.show();
    }
}
