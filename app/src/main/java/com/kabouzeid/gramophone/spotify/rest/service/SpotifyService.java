package com.kabouzeid.gramophone.spotify.rest.service;

import androidx.annotation.Nullable;
import com.kabouzeid.gramophone.spotify.rest.model.SpotifyArtist;
import com.kabouzeid.gramophone.spotify.rest.model.SpotifyArtists;
import com.kabouzeid.gramophone.spotify.rest.model.SpotifyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * @author mabad
 */
public interface SpotifyService {

    String BASE_QUERY_ARTIST = "search?type=artist";

    //    @Headers({
//            "Authorization: Bearer BQDXcFSun0bKTL5Sc-DLsPn7Ze1sR-tSWa0Q6_txuv6PGBpDmVOHaFA1eCOEtQm4tEen3Zt-lNbKtXdmk22z-ZiYtFlrTQmuFw8CfQAtnHb6AF97IJ7oPUxA8jB_wmsdXjyzdXdnL3zYTTjJntyfhK9yAZXUqspvDg"
//    })
    @GET(BASE_QUERY_ARTIST)
    Call<SpotifyResponse<SpotifyArtists<SpotifyArtist>>> getArtistInfo(@Header("Authorization") String bearer, @Query("q") String artistName, @Nullable @Header("Cache-Control") String cacheControl);


}