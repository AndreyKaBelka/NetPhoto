package com;

import com.client.Client;
import explorer.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    public TextField inputString;

    @FXML
    public Button sendButton;

    @FXML
    public TextArea chatHistory;

    @FXML
    public TextArea changedArea;

    private Client client;

    private static boolean haveNewMessage = false;
    private static String newText;

    private static boolean haveNewChange = false;
    private static Change change;

    private int userNumber;

    public static void setText(String newMessage) {
        newText = newMessage;
        haveNewMessage = true;
    }

    public static void setChange(Change newChange) {
        haveNewChange = true;
        change = newChange;
    }

    public void setClient(Client client1, int userNumber1) {
        client = client1;
        userNumber = userNumber1;
    }

    @FXML
    void initialize() {
        changedArea.setEditable(false);
        sendButton.setOnMouseClicked(mouseEvent -> {
            if (!inputString.getText().trim().isEmpty() && !inputString.getText().trim().isBlank()) {
                try {
                    client.sendText("Пользователь " + userNumber + ": " + inputString.getText().trim());

                } catch (Exception e) {
                    setText("Вы пока одни в сессии! Подождите пользователя 2");
                }
                chatHistory.setText(chatHistory.getText() + "\n" + "Пользователь " + userNumber + ": " + inputString.getText().trim());
                inputString.setText("");
            }
        });

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (haveNewMessage) {
                    chatHistory.setText(chatHistory.getText() + "\n" + newText);
                    haveNewMessage = false;
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (haveNewChange) {
                    changedArea.setText(changedArea.getText() + "\n" + change);
                    haveNewChange = false;
                }
            }
        });

        thread1.setDaemon(true);
        thread1.start();
    }
}
