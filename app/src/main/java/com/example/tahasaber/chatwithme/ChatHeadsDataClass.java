package com.example.tahasaber.chatwithme;

/**
 * Created by TahaSaber on 7/10/2017.
 */

public class ChatHeadsDataClass {

    private String name;
    private String imgUri;
    private String lastMessageTime;
    private String lastMessage;

    public ChatHeadsDataClass(String name, String imgUri, String lastMessageTime, String lastMessage) {
        this.name = name;
        this.imgUri = imgUri;
        this.lastMessageTime = lastMessageTime;
        this.lastMessage = lastMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}


