package com.yudapratama.contact_apps_jenius_test.res;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public enum RestApi {

    BASE_URL("https://simple-contact-crud.herokuapp.com/");

    private static WeakReference<Retrofit.Builder> retrofitBuilder;
    private static WeakReference<OkHttpClient.Builder> okHttpClientBuilder;

    @NonNull
    private final String baseUrl;

    RestApi(@NonNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public <Call> Call create(@NonNull Class<Call> callGroup) {
        return getRetrofitBuilder()
                .baseUrl(baseUrl)
                .client(getOkHttpClientBuilder().build())
                .build()
                .create(callGroup);
    }

    public <Call> Call create(@NonNull Class<Call> callGroup, int timeout) {
        return getRetrofitBuilder()
                .baseUrl(baseUrl)
                .client(getOkHttpClientBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS).build())
                .build()
                .create(callGroup);
    }

    private Retrofit.Builder getRetrofitBuilder() {
        if (retrofitBuilder != null && retrofitBuilder.get() != null)
            return retrofitBuilder.get();
        retrofitBuilder = new WeakReference<>(new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()));
        return getRetrofitBuilder();
    }

    private OkHttpClient.Builder getOkHttpClientBuilder() {
        if (okHttpClientBuilder != null && okHttpClientBuilder.get() != null)
            return okHttpClientBuilder.get();
        okHttpClientBuilder = new WeakReference<>(new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)));
        return getOkHttpClientBuilder();
    }
}
