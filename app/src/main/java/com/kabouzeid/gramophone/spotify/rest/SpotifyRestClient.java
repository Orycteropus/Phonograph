package com.kabouzeid.gramophone.spotify.rest;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kabouzeid.gramophone.spotify.rest.service.SpotifyService;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.Locale;

public class SpotifyRestClient {

    private final String BASE_URL = "https://api.spotify.com/v1/";

    private SpotifyService apiService;


    public SpotifyRestClient(@NonNull Context context) {
        this(createDefaultOkHttpClientBuilder(context).build());
    }

    public SpotifyRestClient(@NonNull Call.Factory client) {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = restAdapter.create(SpotifyService.class);
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

    public SpotifyService getApiService() {
        return apiService;
    }
}
