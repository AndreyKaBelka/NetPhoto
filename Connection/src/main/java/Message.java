import com.files.Folder;
import com.files.Photo;

import java.io.IOException;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType msgType;
    private final String text;
    private final Photo photo;
    private final Folder folder;
    private final int userId;


    public Message(MessageType msgType, String text, int userId) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = userId;
        this.folder = null;
    }

    public Message(MessageType msgType, Photo photo, int userId) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = userId;
        this.folder = null;
    }

    public Message(MessageType msgType, Folder folder, int userId) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = userId;
        this.folder = folder;
    }

    public Message(MessageType msgType, int userId) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = userId;
        this.folder = null;

    }

    public Message(MessageType msgType, String text) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = -1;
        this.folder = null;
    }

    public Message(MessageType msgType, Photo photo) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = -1;
        this.folder = null;
    }

    public Message(MessageType msgType, Folder folder) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = -1;
        this.folder = folder;
    }

    public Message(MessageType msgType) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = -1;
        this.folder = null;
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

    public Folder getFolder() {
        return folder;
    }

    @Override
    public String toString() {
        return text;
    }
}
