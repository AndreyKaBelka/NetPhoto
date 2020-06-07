package com;

import com.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private static boolean haveNewMessage = false;
    private static String newText;
    @FXML
    public TextField inputString;
    @FXML
    public Button sendButton;
    @FXML
    public TextArea chatHistory;
    @FXML
    public TextArea changedArea;
    private Client client;
    private int userNumber;

    public static void setText(String newMessage) {
        newText = newMessage;
        haveNewMessage = true;
    }

    @FXML
    void initialize() {
        sendButton.setOnMouseClicked(mouseEvent -> {
            if (!inputString.getText().trim().isEmpty() && !inputString.getText().trim().isBlank()) {
                client.sendText("Пользователь " + userNumber + ": " + inputString.getText().trim());
                chatHistory.setText(chatHistory.getText() + "\n" + "Пользователь " + userNumber + ": " + inputString.getText().trim());
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
    }

    public void setClient(Client client1, int userNumber1) {
        client = client1;
        userNumber = userNumber1;
    }
}
