package com.kabouzeid.gramophone.spotify.rest.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SpotifyArtists<T> {

    @Expose
    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
