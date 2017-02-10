package io.github.niyamatalmass.recomendations.api;

import io.github.niyamatalmass.recomendations.model.ActiveListings;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by niyamat on 1/29/17.
 */

public interface Api {

    @GET("listings/active")
    Call<ActiveListings> activeListings(@Query("includes") String includes, @Query("api_key") String api_key);
}
