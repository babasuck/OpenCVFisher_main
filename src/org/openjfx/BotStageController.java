package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BotStageController {
    @FXML
    public Label titleField;
    public Label difNumLabel;
    @FXML
    public ImageView bobberImage;
    @FXML
    public Label fishNumField;
    @FXML
    public Label failsNumField;
    public Label timeLabel;
    public Label timeoutLabel;

    public void initialize() {
    }

    public void setBobberImage(Image img) {
        bobberImage.setFitHeight(img.getHeight());
        bobberImage.setFitWidth(img.getWidth());
        bobberImage.setImage(img);
    }

    public void setDifNum(double difNum) {
        difNumLabel.setText(String.format("%.2f", difNum));
    }

    public void setFishNumField(int fishNumField) {
        this.fishNumField.setText(String.valueOf(fishNumField));
    }

    public void setFailsNumField(int failsNumField) {
        this.failsNumField.setText(String.valueOf(failsNumField));
    }

    public void setTimeoutLabel(int timeout) {
        this.timeoutLabel.setText(String.valueOf(timeout));
    }

    public void setTimeLabel(int time) {
        this.timeLabel.setText(String.valueOf(time));
    }
}
