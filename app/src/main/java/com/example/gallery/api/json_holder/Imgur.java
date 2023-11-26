package com.example.gallery.api.json_holder;

public class Imgur {
    private DataImgur data;
    private boolean success;
    private int status;

    public Imgur(DataImgur data, boolean success, int status) {
        this.data = data;
        this.success = success;
        this.status = status;
    }

    public DataImgur getData() {
        return data;
    }

    public void setData(DataImgur data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}