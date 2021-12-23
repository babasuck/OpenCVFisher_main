package org.openjfx;


import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.IIOException;
import java.awt.*;
import java.io.IOException;

public class BotStage {
    private BotStageController botStageController;
    public BotStage(Double sens, Point fishActionCoord, Rectangle AOI, Point destroyFishPosition) {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BotStage.fxml"));
        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setX(0);
        stage.setY(0);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        botStageController = loader.getController();
        Thread bot = new Bot(sens, fishActionCoord, AOI, destroyFishPosition, this);
        botStageController.titleField.setText(bot.getName());
        stage.setOnCloseRequest(windowEvent -> bot.interrupt());
    }

    public BotStageController getBotStageController() {
        return botStageController;
    }

}
