package org.openjfx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

import java.util.Objects;

public class Main extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    Parent root;
    FXMLLoader loader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        root = loader.load();
        primaryStage.setTitle(BotUtil.generateString(10));
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("icon.ico"));
    }
}
