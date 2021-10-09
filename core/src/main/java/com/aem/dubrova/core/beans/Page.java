package com.aem.dubrova.core.beans;

import com.aem.dubrova.core.constants.Constants;

public class Page {

    private String title;
    private String image;
    private String text;
    private String path;

    public Page(String title, String image, String text, String path) {
        this.title = title;
        this.image = image;
        this.text = text;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
