//package com.album;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ProgressBar;
//import javafx.scene.control.TextArea;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.Pane;
//import javafx.stage.DirectoryChooser;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.CharBuffer;
//import java.util.Date;
//import java.util.concurrent.atomic.AtomicReference;
//
//
//public class Controller {
//
//    Double timeNow = 0.0;
//
//    @FXML
//    private Button ButtonUser1;
//
//    @FXML
//    private Button ButtonUser2;
//
//    @FXML
//    private Pane Name;
//
//    @FXML
//    private TextArea TextGeneratedKey;
//
//    @FXML
//    private TextArea TextPath;
//
//    @FXML
//    private TextArea TextTemp;
//
//    @FXML
//    private Button ButtonStart;
//
//    @FXML
//    private Button ButtonBrowse;
//
//    @FXML
//    private ImageView ImageOk;
//
//    @FXML
//    private ImageView ImageBan;
//
//    @FXML
//    private TextArea TextKey;
//
//    @FXML
//    private Button ButtonConnect;
//
//    @FXML
//    private ImageView ImageLoading;
//
//    @FXML
//    private ImageView ImageBan1;
//
//    @FXML
//    private TextArea TextPath1;
//
//    @FXML
//    private TextArea TextTemp1;
//
//    @FXML
//    private Button ButtonStart1;
//
//    @FXML
//    private Button ButtonBrowse1;
//
//    @FXML
//    private ProgressBar SessionTime;
//
//    @FXML
//    private AnchorPane mainmenu;
//
//    @FXML
//    private AnchorPane connectedpane;
//
//    @FXML
//    private AnchorPane servermenu;
//
//    @FXML
//    private AnchorPane keymenu;
//
//    @FXML
//    void initialize() {
//        AtomicReference<String> pathdir = new AtomicReference<>(new String( ));
////        AtomicReference<Client> client = new AtomicReference<>(new Client( ));
//
//        ButtonUser1.setOnAction(actionEvent -> {
//            mainmenu.setVisible(false);
//            servermenu.setVisible(true);
//        });
//
//        ButtonBrowse.setOnAction(actionEvent -> {
//            final DirectoryChooser dirChooser = new DirectoryChooser();
//            File dir = dirChooser.showDialog(null);
//            if (dir != null) {
//                TextPath.setText(dir.getAbsolutePath());
//                pathdir.set(dir.getAbsolutePath( ));
//            } else {
//                TextPath.setText(null);
//            }
//        });
//
//        ButtonStart.setOnAction(actionEvent -> {
//            ImageOk.setVisible(false);
//            ImageBan.setVisible(false);
//            Server serv = new Server();
//            TextGeneratedKey.setText(Crypt.encrypt("00"));
//            System.out.println(pathdir.toString());
//            new Thread(() -> {
//            try {
//                serv.server_start(pathdir.toString());
//                ImageOk.setVisible(true);
//            } catch (IOException e) {
//                e.printStackTrace( );
//                ImageBan.setVisible(true);
//            }}).start();
//            new Thread(() -> {
//                SessionTime.setVisible(true);
//                while(true) {
//                    Date date = new Date();
//                    timeNow = (double) date.getTime( )%100000/100000;
//                    SessionTime.setProgress(timeNow);
//                    if (timeNow == 0) TextGeneratedKey.setText(Crypt.encrypt("00"));
//                }
//                }).start();
//        });
//
//        ButtonUser2.setOnAction(actionEvent -> {
//            mainmenu.setVisible(false);
//            keymenu.setVisible(true);
//        });
//
//        ButtonConnect.setOnAction(actionEvent -> {
//            ImageLoading.setVisible(true);
//            try {
//                client.get().connect(Crypt.decrypt(TextKey.getText( )));
//                connectedpane.setVisible(true);
//                ImageLoading.setVisible(false);
//                keymenu.setVisible(false);
//            } catch (StringIndexOutOfBoundsException | IOException | ArrayIndexOutOfBoundsException e) {
//                e.printStackTrace();
//                ImageBan1.setVisible(true);
//            }
//        });
//
//        ButtonBrowse1.setOnAction(actionEvent -> {
//            final DirectoryChooser dirChooser = new DirectoryChooser();
//            File dir = dirChooser.showDialog(null);
//            if (dir != null) {
//                TextPath1.setText(dir.getAbsolutePath());
//                pathdir.set(dir.getAbsolutePath());
//            } else {
//                TextPath1.setText(null);
//            }
//        });
//
//        ButtonStart1.setOnAction(actionEvent -> {
//            new Thread(() -> {
//                try {
//                    ImageLoading.setVisible(true);
//                    client.get().download(pathdir.get());
//                    ImageOk.setVisible(true);
//                    ImageLoading.setVisible(false);
//                } catch (IOException e) {
//                    e.printStackTrace( );
//                    ImageBan.setVisible(true);
//                    ImageLoading.setVisible(false);
//                }}).start();
//        });
//    }
//}