package com.kabouzeid.gramophone.spotify.rest.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SpotifyArtist {

    @Expose
    private String name;

    @Expose
    private List<SpotifyImage> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpotifyImage> getImages() {
        return images;
    }

    public void setImages(List<SpotifyImage> images) {
        this.images = images;
    }
}
