package org.openjfx;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;


public class MainController {
    @FXML
    AnchorPane mainPane;
    @FXML
    Button setButton;
    @FXML
    Button lookButton;
    @FXML
    Button startButton;
    @FXML
    Button setFishActionButton;
    Point fishActionPosition;
    @FXML
    Label fishActionField;
    @FXML
    Label fishNumField;
    @FXML
    Button stopButton;
    @FXML
    Label methodField;
    @FXML
    TextField sensField;
    @FXML
    Button applyAOIButton;
    @FXML
    Label firstPosField;
    @FXML
    Label secondPosField;
    @FXML
    ImageView bobberField;
    @FXML
    Label difLabel;
    @FXML
    RadioMenuItem debugSwitcher;
    Point firstPos, secondPos;
    String method = "color";
    Rectangle AOI;
    WindowImage WI;
    WindowImageController WIC;

    @FXML
    public void setButtonAction() {
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                int x, y;
                String code = keyEvent.getCode().toString();
                if (code.equals("DIGIT1")) {
                    firstPos = MouseInfo.getPointerInfo().getLocation();
                    x = firstPos.x;
                    y = firstPos.y;
                    firstPosField.setText(x + " " + y);
                } else if (code.equals("DIGIT2")) {
                    secondPos = MouseInfo.getPointerInfo().getLocation();
                    x = secondPos.x;
                    y = secondPos.y;
                    secondPosField.setText(x + " " + y);
                }
            }
        });
    }

    public void applyAOIButtonAction() {
        if (!firstPosField.getText().isEmpty() && !secondPosField.getText().isEmpty()) {
            int w = secondPos.x - firstPos.x;
            int h = secondPos.y - firstPos.y;
            if (w <= 0 && h <= 0) {
                Alert al = new Alert(AlertType.ERROR, "Invalid Dimension", ButtonType.OK);
                al.showAndWait();
                return;
            }
            AOI = new Rectangle(firstPos, new Dimension(w, h));
            mainPane.setOnKeyPressed(null);
            lookButton.setDisable(false);
            startButton.setDisable(false);
        }
    }

    @FXML
    public void setFishActionButtonAction() {
        mainPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString();
                if (code.equals("DIGIT3")) {
                    fishActionPosition = MouseInfo.getPointerInfo().getLocation();
                    fishActionField.setText(fishActionPosition.x + " " + fishActionPosition.y);
                }
            }
        });
    }

    @FXML
    public void lookButtonAction() throws IOException, InterruptedException {
        mainPane.setOnKeyPressed(null);
        BufferedImage bf = BotUtil.grabScreen(AOI);
        Image img = SwingFXUtils.toFXImage(bf, null);
        WI = new WindowImage(AOI.width, AOI.height);
        WI.show();
        WIC = WI.getController();
        WIC.setImage(img);
    }

    @FXML
    public void startButtonAction() throws IOException, IllegalAccessException {
        if (AOI != null) {
//            WI = new WindowImage(AOI.width, AOI.height);
//            WIC = WI.getController();
            Bot bot;
            MainController MI = this;
            if(sensField.getText().isEmpty())
                sensField.setText("3.5");
            double sens = Double.parseDouble(sensField.getText());
            bot = new Bot(sens, fishActionPosition, debugSwitcher.isSelected(), AOI, MI);
            bot.start();
            stopButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    bot.interrupt();
                    startButton.setDisable(false);
                    stopButton.setDisable(true);
                    setButton.setDisable(false);
                    lookButton.setDisable(false);
                    applyAOIButton.setDisable(false);
                    setFishActionButton.setDisable(false);
                    sensField.setDisable(false);
                }
            });
        }
        stopButton.setDisable(false);
        startButton.setDisable(true);
        setButton.setDisable(true);
        lookButton.setDisable(true);
        applyAOIButton.setDisable(true);
        setFishActionButton.setDisable(true);
        sensField.setDisable(true);
    }

    @FXML
    public void stopButtonAction() {
    }

    @FXML
    public void colorMethodMenuAction() {
        method = "color";
        methodField.setText("Color");
    }

    @FXML
    public void templateMethodMenuAction() {
        method = "template";
        methodField.setText("Template");
    }

    public void setBobberField(Image img) {
        bobberField.setFitHeight(img.getHeight());
        bobberField.setFitWidth(img.getWidth());
        bobberField.setImage(img);
    }

    public void setFishNumField(String text) {
        fishNumField.setText(text);
    }
}
