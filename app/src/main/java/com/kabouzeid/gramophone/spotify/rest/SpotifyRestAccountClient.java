package com.kabouzeid.gramophone.spotify.rest;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kabouzeid.gramophone.spotify.rest.service.SpotifyAccessTokenService;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.Locale;

public class SpotifyRestAccountClient {

    public static final String CLIENT_ID = "4a1226fe3bf3457a956c4f6629172d52";
    public static final String CLIENT_SECRET = "652c8518a7f04837afde7972aaa6c8c4";
    public static final String REFRESH_TOKEN = "AQBJjrNmECvUp7EZQlGN3isf3tSd1Oai1tc4a68p0Mve79UYTqN10RySEa-tHOOVgt3zSYJkxkd5RoIngwbYJrxPlGXrAI4D2f_f16UzQTnzFaqIzae3Jm5aepCtWEt-E3orWQ";
    private final String BASE_URL = "https://accounts.spotify.com/api/";

    private SpotifyAccessTokenService apiService;


    public SpotifyRestAccountClient(@NonNull Context context) {
        this(createDefaultOkHttpClientBuilder(context).build());
    }

    public SpotifyRestAccountClient(@NonNull Call.Factory client) {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = restAdapter.create(SpotifyAccessTokenService.class);
    }

    @Nullable
    public static Cache createDefaultCache(Context context) {
        File cacheDir = new File(context.getCacheDir().getAbsolutePath(), "/okhttp-spotify/");
        if (cacheDir.mkdirs() || cacheDir.isDirectory()) {
            return new Cache(cacheDir, 1024 * 1024 * 10);
        }
        return null;
    }

    public static Interceptor createCacheControlInterceptor() {
        return chain -> {
            Request modifiedRequest = chain.request().newBuilder()
                    .addHeader("Cache-Control", String.format(Locale.getDefault(), "max-age=%d, max-stale=%d", 31536000, 31536000))
                    .build();
            return chain.proceed(modifiedRequest);
        };
    }

    public static OkHttpClient.Builder createDefaultOkHttpClientBuilder(Context context) {
        return new OkHttpClient.Builder()
                .cache(createDefaultCache(context))
                .addInterceptor(createCacheControlInterceptor());
    }

    public SpotifyAccessTokenService getApiService() {
        return apiService;
    }
}
