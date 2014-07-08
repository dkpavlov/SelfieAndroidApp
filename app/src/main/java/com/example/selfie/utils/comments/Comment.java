package com.example.selfie.utils.comments;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class Comment {
    private String text;
    private String user;

    public Comment(String text, String user) {
        this.text = text;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
