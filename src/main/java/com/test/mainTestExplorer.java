package com.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainTestExplorer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource("/fxml/tests.fxml"));//TODO: Никита если ты это увидишь, то пофикси это гавно, он не видит файл
            Parent root = fxmlLoader.getRoot();
            primaryStage.setTitle("TEST!!!");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }

}
