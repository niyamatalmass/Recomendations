package io.github.niyamatalmass.recomendations.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by niyamat on 1/29/17.
 */

public class Etsy {
    public static final String API_KEY = "y9b0uj1093qb31q44e5ds4as";

    public static Api getApi() {

        /*OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("api_key", API_KEY)
                                .build();
                        return  chain.proceed(request);
                    }
                }).build();*/

        return new Retrofit.Builder()
                .baseUrl("https://openapi.etsy.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);

    }
}
