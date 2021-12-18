package org.openjfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowImage {
    Parent root;
    FXMLLoader loader;
    Scene scene;
    Stage stage;

    public WindowImage(int w, int h) {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("WindowImage.fxml"));
        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(root, w, h);
        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);

    }

    public WindowImageController getController() {
        return loader.getController();
    }

    public void show() {
        stage.show();
    }
}
