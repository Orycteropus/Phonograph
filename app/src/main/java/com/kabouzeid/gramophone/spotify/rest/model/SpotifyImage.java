package com.kabouzeid.gramophone.spotify.rest.model;

import com.google.gson.annotations.Expose;

public class SpotifyImage {

    @Expose
    private String height;

    @Expose
    private String url;

    @Expose
    private String width;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
