package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WindowImageController {
    @FXML
    ImageView imageView;

    public void initialize() {

    }

    public void setImage(Image img) {
        imageView.setFitWidth(img.getWidth());
        imageView.setFitHeight(img.getHeight());
        imageView.setImage(img);
    }
}
