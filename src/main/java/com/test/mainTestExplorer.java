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
            System.out.println(getClass().getResource("/tests.fxml").getPath());
            Parent root = FXMLLoader.load(getClass().getResource("/tests.fxml"));
            primaryStage.setTitle("TEST!!!");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }

}
