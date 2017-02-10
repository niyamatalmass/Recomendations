package io.github.niyamatalmass.recomendations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.net.HttpURLConnection;

import io.github.niyamatalmass.recomendations.api.Etsy;
import io.github.niyamatalmass.recomendations.google.GoogleServicesHelper;
import io.github.niyamatalmass.recomendations.model.ActiveListings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_ACTIVE_LISTINGS = "STATE_ACTIVE_LISTINGS";
    private ActiveListings listings;
    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorView;
    private GoogleServicesHelper googleServicesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String keystring = "y9b0uj1093qb31q44e5ds4as";
        String googleClientId = "289423410359-5a5m6sti43vsp9s1lau0gol9hust6563.apps.googleusercontent.com";
        String etsyApiUrl = "GET https://openapi.etsy.com/v2/listings/active?api_key={YOUR_API_KEY}";

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        errorView = (TextView) findViewById(R.id.error_view);

        showLoading();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        ListingAdapter adapter = new ListingAdapter(this);
        recyclerView.setAdapter(adapter);

        googleServicesHelper = new GoogleServicesHelper(this, adapter);
        if (savedInstanceState == null) {
            fetchListing();
        } else {
            if (savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
                ActiveListings listings = savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS);
                ((ListingAdapter) recyclerView.getAdapter()).swap(listings);
                showList();
            } else {
                fetchListing();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleServicesHelper.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (listings != null) {
            outState.putParcelable(STATE_ACTIVE_LISTINGS, listings);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleServicesHelper.handleActivityResult(requestCode, resultCode, data);

        if (requestCode == ListingAdapter.REQUEST_CODE_PLUS_ONE) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void fetchListing() {
        Etsy.getApi()
                .activeListings("Images,Shop", Etsy.API_KEY)
                .enqueue(new Callback<ActiveListings>() {
                    @Override
                    public void onResponse(Call<ActiveListings> call, Response<ActiveListings> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            showList();
                            listings = response.body();
                            ((ListingAdapter) recyclerView.getAdapter()).swap(listings);
                        } else {
                            showError(response.code() + "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ActiveListings> call, Throwable t) {
                        showError(t.getMessage());
                    }
                });
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showList() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    public void showError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorMessage);

    }
}
