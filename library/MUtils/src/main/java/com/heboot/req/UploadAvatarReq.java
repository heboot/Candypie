package com.heboot.req;

/**
 * Created by heboot on 2018/3/9.
 */

public class UploadAvatarReq {

    private String path;
    private String ext;
    private long size;
    private int width;
    private int height;


    public UploadAvatarReq() {

    }

    public UploadAvatarReq(String ext, int height, int width, String path, long size) {
        this.ext = ext;
        this.height = height;
        this.path = path;
        this.size = size;
        this.width = width;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
