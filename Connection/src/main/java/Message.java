import explorer.Photo;

import java.io.IOException;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType msgType;
    private final String text;
    private final Photo photo;
    private final int userId;


    public Message(MessageType msgType, String text, int userId) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = userId;

    }

    public Message(MessageType msgType, Photo photo, int userId) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = userId;

    }

    public Message(MessageType msgType, int userId) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = userId;
    }

    public Message(MessageType msgType, String text) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = -1;

    }

    public Message(MessageType msgType, Photo photo) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = -1;

    }

    public Message(MessageType msgType) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = -1;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public Photo getPhoto() {
        return photo;
    }

    public String getText() {
        return text;
    }

    public int getUserId() throws IOException {
        if (userId == -1) throw new IOException("Неверный id пользователя");
        return userId;
    }
}
