package com.kabouzeid.gramophone.spotify.rest.model;

public class SpotifyResponse<T> {

    private T artists;

    public T getArtists() {
        return artists;
    }

    public void setArtists(T artists) {
        this.artists = artists;
    }
}
