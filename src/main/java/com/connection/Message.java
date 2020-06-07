package com.connection;

import com.files.Folder;
import com.files.Photo;
import explorer.Changes;

import java.io.IOException;
import java.io.Serializable;

/**
 * Данный класс предназначен для получения и отправки пользователями сообщений(фото, папок)
 */
public class Message implements Serializable {
    private final MessageType msgType;//тип передаваемого сообщения
    private final String text;//текст сообщения
    private final Photo photo;//фото сообщения
    private final Folder folder;//папка
    private final long userId;//id пользователя отправившего сообщение
    private final String tokenOfSession;//токен сессии, в которой происходит обмен сообщениями
    private int cnt_photo;
    private Changes changes;


    public Message(MessageType msgType, String text, long userId, String tokenOfSession) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = userId;
        this.folder = null;
        this.tokenOfSession = tokenOfSession;
    }

    public Message(MessageType msgType, Photo photo, int cnt_photo, long userId, String tokenOfSession) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = userId;
        this.folder = null;
        this.tokenOfSession = tokenOfSession;
        this.cnt_photo = cnt_photo;
    }

    public Message(MessageType msgType, Folder folder, long userId, String tokenOfSession) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = userId;
        this.folder = folder;
        this.tokenOfSession = tokenOfSession;
    }

    public Message(MessageType msgType, long userId) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = userId;
        this.folder = null;
        this.tokenOfSession = null;
    }

    public Message(MessageType msgType, String text) {
        this.msgType = msgType;
        this.text = text;
        this.photo = null;
        this.userId = -1;
        this.folder = null;
        this.tokenOfSession = null;

    }

    public Message(MessageType msgType, Photo photo) {
        this.msgType = msgType;
        this.photo = photo;
        this.text = null;
        this.userId = -1;
        this.folder = null;
        this.tokenOfSession = null;

    }

    public Message(MessageType msgType, Folder folder) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = -1;
        this.folder = folder;
        this.tokenOfSession = null;

    }

    public Message(MessageType msgType) {
        this.msgType = msgType;
        this.photo = null;
        this.text = null;
        this.userId = -1;
        this.folder = null;
        this.tokenOfSession = null;
    }

    public Message(MessageType msgType, Changes changes, long userId, String tokenOfSession) {
        this.msgType = msgType;
        this.text = null;
        this.photo = null;
        this.userId = userId;
        this.folder = null;
        this.tokenOfSession = tokenOfSession;
        this.changes = changes;
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

    public long getUserId() throws IOException {
        if (userId == -1) throw new IOException("Неверный id пользователя");
        return userId;
    }

    public String getTokenOfSession() {
        return tokenOfSession;
    }

    public Folder getFolder() {
        return folder;
    }

    public int getCnt_photo() {
        return cnt_photo;
    }

    @Override
    public String toString() {
        return text;
    }

    public Changes getChanges() {
        return changes;
    }
}
