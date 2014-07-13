package com.example.selfie.utils.data;

import android.net.Uri;

/**
 * Created by dkpavlov on 7/11/14.
 */
public class MySelfie {
    private long id;
    private String selfieId;
    private String thumbName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSelfieId() {
        return selfieId;
    }

    public void setSelfieId(String selfieId) {
        this.selfieId = selfieId;
    }

    public String getThumbName() {
        return thumbName;
    }

    public void setThumbName(String thumbName) {
        this.thumbName = thumbName;
    }
}
