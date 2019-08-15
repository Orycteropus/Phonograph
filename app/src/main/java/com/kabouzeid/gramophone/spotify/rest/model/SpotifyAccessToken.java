package com.kabouzeid.gramophone.spotify.rest.model;

import com.google.gson.annotations.Expose;

public class SpotifyAccessToken {

    @Expose
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
