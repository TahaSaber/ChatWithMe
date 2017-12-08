package com.example.tahasaber.chatwithme;

/**
 * Created by TahaSaber on 7/6/2017.
 */

public class UserDataClass {

    private String name;
    private String email;
    private String id;
    private String description;
    private String photoUrl;

    public UserDataClass() {

    }

    public UserDataClass(String name, String email, String id, String photoUrl) {
        this.name = name;
        this.email = email;
        this.id = id;
        description = "I am using ChatWithMEApp";
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {

        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}

