package com.example.selfie.utils.data;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class Selfie {

    private long id;
    private String path;
    private String thumbPath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}
