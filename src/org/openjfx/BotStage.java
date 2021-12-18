package org.openjfx;


import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.IIOException;
import java.io.IOException;

public class BotStage {
    Parent root;
    FXMLLoader loader;
    Scene scene;
    Stage stage = new Stage();
    BotStageController botStageController;

    public BotStage(String title, Thread thread) {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BotStage.fxml"));
        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.show();
        botStageController = loader.getController();
        botStageController.titleField.setText(title);
        stage.setOnCloseRequest(windowEvent -> {
            thread.interrupt();
        });
    }

}
