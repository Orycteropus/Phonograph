package com.kabouzeid.gramophone.spotify.rest.service;

import com.kabouzeid.gramophone.spotify.rest.model.SpotifyAccessToken;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author mabad
 */
public interface SpotifyAccessTokenService {

    @Headers({
            "Content-Type: application/x-www-form-urlencoded"
    })
//    @POST("token?grant_type=refresh_token&refresh_token=" + SpotifyRestClient.REFRESH_TOKEN + "&client_id=" + SpotifyRestClient.CLIENT_ID + "&client_secret=" + SpotifyRestClient.CLIENT_SECRET)
    @POST("token?grant_type=refresh_token&refresh_token=AQBJjrNmECvUp7EZQlGN3isf3tSd1Oai1tc4a68p0Mve79UYTqN10RySEa-tHOOVgt3zSYJkxkd5RoIngwbYJrxPlGXrAI4D2f_f16UzQTnzFaqIzae3Jm5aepCtWEt-E3orWQ&client_id=4a1226fe3bf3457a956c4f6629172d52&client_secret=652c8518a7f04837afde7972aaa6c8c4")
    Call<SpotifyAccessToken> getAccessToken();

}