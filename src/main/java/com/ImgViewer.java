package com;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImgViewer {

    @FXML
    private ImageView imgView;

    public void setImage(Image img, double width, double height) {
        imgView.setFitWidth(width);
        imgView.setFitHeight(height);
        imgView.setImage(img);
    }
}
