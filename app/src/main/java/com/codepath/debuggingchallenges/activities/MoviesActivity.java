package com.codepath.debuggingchallenges.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.debuggingchallenges.R;
import com.codepath.debuggingchallenges.adapters.MoviesAdapter;
import com.codepath.debuggingchallenges.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Headers;

public class MoviesActivity extends AppCompatActivity {

    public static final String TAG = "MoviesActivity";
    private static final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    RecyclerView rvMovies;
    MoviesAdapter adapter;
    ArrayList<Movie> movies;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        rvMovies = findViewById(R.id.rvMovies);

        movies = new ArrayList<>();

        // Create the adapter to convert the array to views
        adapter = new MoviesAdapter(this, movies);

        client = new AsyncHttpClient();

        // Attach the adapter to a ListView
        rvMovies.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // RecyclerView setup, layout manager and adapter
        rvMovies.setLayoutManager(layoutManager);



        fetchMovies();
    }


    private void fetchMovies() {
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.e(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray result = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Result: " + result.toString());
                    movies.addAll(Movie.fromJSONArray(result));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i(TAG, "Hit JSONException: " + e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG, "onFailure");

            }
        });
    }
}
