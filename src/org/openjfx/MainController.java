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
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;



public class MainController {
    public Label statsField;
    public Label fishField;
    @FXML
    public Label difNumField;
    @FXML
    public AnchorPane mainPane;
    @FXML
    public Button setButton;
    @FXML
    public Button lookButton;
    @FXML
    public Button startButton;
    @FXML
    public Button setFishActionButton;
    public Point fishActionPosition;
    @FXML
    public Label fishActionField;
    @FXML
    public Label fishNumField;
    @FXML
    public Button stopButton;
    @FXML
    public Label methodField;
    @FXML
    public TextField sensField;
    @FXML
    public Button applyAOIButton;
    @FXML
    public Label firstPosField;
    @FXML
    public Label secondPosField;
    @FXML
    public ImageView bobberField;
    @FXML
    public Label difLabel;
    @FXML
    RadioMenuItem debugSwitcher;
    Point firstPos, secondPos;
    String method = "color";
    Rectangle AOI;
    //WindowImage WI;
    //WindowImageController WIC;

    /**
     * Получить координаты для AOI и удочки.
     * Взять координаты X - на кнопку 1
     * Взять координаты Y - на кнопку 2
     * Взять координаты удочки - на кнопку 3
     */
    @FXML
    public void setButtonAction() {
        mainPane.setOnKeyPressed(keyEvent -> {
            int x, y;
            String code = keyEvent.getCode().toString();
            switch (code) {
                case "DIGIT1" -> {
                    firstPos = MouseInfo.getPointerInfo().getLocation();
                    x = firstPos.x;
                    y = firstPos.y;
                    firstPosField.setText(x + " " + y);
                }
                case "DIGIT2" -> {
                    secondPos = MouseInfo.getPointerInfo().getLocation();
                    x = secondPos.x;
                    y = secondPos.y;
                    secondPosField.setText(x + " " + y);
                }
                case "DIGIT3" -> {
                    fishActionPosition = MouseInfo.getPointerInfo().getLocation();
                    fishActionField.setText(fishActionPosition.x + " " + fishActionPosition.y);
                }
            }
        });
    }

    /**
     * Применить координаты AOI и построить его.
     */
    public void applyAOIButtonAction() {
        mainPane.setOnKeyPressed(null); // Отключаем чтение событий с клавы.
        if (firstPos != null && secondPos != null && fishActionPosition != null) {
            int w = secondPos.x - firstPos.x;
            int h = secondPos.y - firstPos.y;
            if (w <= 0 && h <= 0) {
                new Alert(AlertType.ERROR, "Invalid Dimension", ButtonType.OK).showAndWait();
                return;
            }
            // Инициализируем AOI
            AOI = new Rectangle(firstPos, new Dimension(w, h));
            lookButton.setDisable(false);
            startButton.setDisable(false);
        }
    }
    /**
     * Посмотреть AOI
     */
    @FXML
    public void lookButtonAction() {
        mainPane.setOnKeyPressed(null); // Отключаем чтение событий с клавы.
        BufferedImage bf = BotUtil.grabScreen(AOI);
        if(bf == null)
            throw new NullPointerException();
        WindowImage wi = new WindowImage(bf.getWidth(), bf.getHeight());
        wi.getController().setImage(SwingFXUtils.toFXImage(bf, null));
        wi.show();
    }
    @FXML
    public void startButtonAction() throws IOException, IllegalAccessException {
        if (AOI == null || fishActionPosition == null) {
            new Alert(AlertType.ERROR, "Invalid Dimension", ButtonType.OK).showAndWait();
            return;
        }
        if(sensField.getText().isEmpty())
            sensField.setText("3.5");
        double sens = Double.parseDouble(sensField.getText());
        new BotStage(sens, fishActionPosition, AOI);
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
