package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val image = findViewById<ImageView>(R.id.imageView2)


        button.setOnClickListener(){
            val userInput = findViewById<EditText>(R.id.movieNameInput).text.toString()
            getMovieInfo(userInput, image, )
        }
    }
    private fun getMovieInfo(userQuery: String, imageView: ImageView) {
        val apiKey = "c00a0b45aa93c06c47a5135c174cab25"
        val baseUrl = "https://api.themoviedb.org/3/search/movie"

        val client = AsyncHttpClient()

        val params = RequestParams()
        params.put("query", userQuery)
        params.put("api_key", apiKey)

        client.get(baseUrl, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful$json")

                try {
                    val resultsArray = json.jsonObject.getJSONArray("results")
                    if (resultsArray.length() > 0) {
                        // Choose a movie from the array (e.g., the first one)
                        val firstMovie = resultsArray.getJSONObject(0)

                        val movieTitle = firstMovie.optString("title", "")
                        val releaseDate = firstMovie.optString("release_date", "")
                        val rating  = firstMovie.optString("vote_average", "")


                        if(movieTitle.isNotEmpty()){
                            val movieTitleTextView = findViewById<TextView>(R.id.movieTitle)
                            movieTitleTextView.text = "$movieTitle"
                            val releaseDateTextView = findViewById<TextView>(R.id.textView5)
                            releaseDateTextView.text = "Release Date: $releaseDate"
                            val ratingTextView = findViewById<TextView>(R.id.ratingText)
                            ratingTextView.text = "Rating: $rating ⭐"
                        }



                        val backdropPath = firstMovie.optString("backdrop_path", "")

                        if (backdropPath.isNotEmpty()) {
                            // Construct the full image URL using the base URL for movie images
                            val fullImageUrl = "https://image.tmdb.org/t/p/original$backdropPath"

                            // Load the movie image using Glide
                            Glide.with(this@MainActivity)
                                .load(fullImageUrl)
                                .fitCenter()
                                .into(imageView)
                        } else {
                            // Handle the case where backdrop_path is missing in the JSON response
                            Log.d("Movie Error", "Backdrop path not found for the selected movie")
                        }
                    } else {
                        Log.d("Movie Error", "No movies found in the response")
                    }
                } catch (e: JSONException) {
                    // Handle JSON parsing error
                    Log.e("Movie Error", "JSON parsing error: ${e.message}")
                }
            }


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Movie Error", errorResponse)
            }
        })
    }
    }
