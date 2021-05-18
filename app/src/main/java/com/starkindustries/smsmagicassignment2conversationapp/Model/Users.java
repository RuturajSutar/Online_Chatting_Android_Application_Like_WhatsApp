package com.starkindustries.smsmagicassignment2conversationapp.Model;

public class Users {

    private String id;
    private String username;
    private String imageURL;
    private String mobile;
    private String status;

    public Users() {

    }

    public Users(String id, String username, String imageURL, String mobile , String status) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.mobile = mobile;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
