package com.album;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class Controller {

    @FXML
    private Button ButtonUser1;

    @FXML
    private Button ButtonUser2;

    @FXML
    private Pane Name;

    @FXML
    private TextArea TextGeneratedKey;

    @FXML
    private TextArea TextPath;

    @FXML
    private TextArea TextTemp;

    @FXML
    private Button ButtonStart;

    @FXML
    private Button ButtonBrowse;

    @FXML
    private ImageView ImageOk;

    @FXML
    private ImageView ImageBan;

    @FXML
    private TextArea TextKey;

    @FXML
    private Button ButtonConnect;

    @FXML
    private ImageView ImageLoading;

    @FXML
    private ImageView ImageBan1;

    @FXML
    private TextArea TextPath1;

    @FXML
    private TextArea TextTemp1;

    @FXML
    private Button ButtonStart1;

    @FXML
    private Button ButtonBrowse1;

    @FXML
    void initialize() throws IOException {

        AtomicReference<String> pathdir = new AtomicReference<>(new String( ));

        ButtonUser1.setOnAction(actionEvent -> {
            ButtonUser1.setVisible(false);
            ButtonUser2.setVisible(false);
            Name.setVisible(false);
            TextGeneratedKey.setVisible(true);
            TextPath.setVisible(true);
            TextTemp.setVisible(true);
            ButtonStart.setVisible(true);
            ButtonBrowse.setVisible(true);
        });

        ButtonBrowse.setOnAction(actionEvent -> {
            final DirectoryChooser dirChooser = new DirectoryChooser();
            File dir = dirChooser.showDialog(null);
            if (dir != null) {
                TextPath.setText(dir.getAbsolutePath());
                pathdir.set(dir.getAbsolutePath( ));
            } else {
                TextPath.setText(null);
            }
        });

        ButtonStart.setOnAction(actionEvent -> {
            ImageOk.setVisible(false);
            ImageBan.setVisible(false);
            Server serv = new Server();
            TextGeneratedKey.setText(Crypt.encrypt("00"));
            System.out.println(pathdir.toString());
            new Thread(() -> {
            try {
                serv.server_start(pathdir.toString());
                ImageOk.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace( );
                ImageBan.setVisible(true);
            }}).start();
        });

        ButtonUser2.setOnAction(actionEvent -> {
            ButtonUser1.setVisible(false);
            ButtonUser2.setVisible(false);
            Name.setVisible(false);
            TextKey.setVisible(true);
            ButtonConnect.setVisible(true);
        });

        ButtonConnect.setOnAction(actionEvent -> {
            TextKey.setVisible(false);
            ButtonConnect.setVisible(false);
            ImageLoading.setVisible(true);
            try {
                Client client = new Client(Crypt.decrypt(TextKey.getText()).substring(8));
                TextPath1.setVisible(true);
                TextTemp1.setVisible(true);
                ButtonStart1.setVisible(true);
                ButtonBrowse1.setVisible(true);
            } catch (StringIndexOutOfBoundsException | IOException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                ImageLoading.setVisible(false);
                TextKey.setVisible(true);
                ButtonConnect.setVisible(true);
                ImageBan1.setVisible(true);
            }
        });
    }
}