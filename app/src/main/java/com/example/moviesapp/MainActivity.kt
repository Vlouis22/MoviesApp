package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var movieList: MutableList<String>
    private lateinit var rvMovies: RecyclerView
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.MovieList)
        movieList = mutableListOf()
        adapter = MovieAdapter(movieList)
        rvMovies.layoutManager = LinearLayoutManager(this)
        rvMovies.adapter = adapter

        val button = findViewById<Button>(R.id.button)
        val image = findViewById<ImageView>(R.id.movie_image)

        button.setOnClickListener {
            val userInput = findViewById<EditText>(R.id.movieNameInput).text.toString()
            getMovieInfo(userInput, image)
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
                try {
                    val movieArray = json.jsonObject.getJSONArray("results")
                    movieList.clear() // Clear the previous movie list
                    for (i in 0 until movieArray.length()) {
                        val movie = movieArray.getJSONObject(i)
                        val title = movie.optString("title", "")
                        val releaseDate = movie.optString("release_date", "")
                        val rating = movie.optString("vote_average", "")
                        movieList.add("$title\nRelease Date: $releaseDate\nRating: $rating ⭐")
                    }
                    adapter.notifyDataSetChanged()

                    if (movieArray.length() > 0) {
                        val firstMovie = movieArray.getJSONObject(0)
                        val backdropPath = firstMovie.optString("backdrop_path", "")

                        if (backdropPath.isNotEmpty()) {
                            val fullImageUrl = "https://image.tmdb.org/t/p/original$backdropPath"
                            Glide.with(this@MainActivity)
                                .load(fullImageUrl)
                                .fitCenter()
                                .into(imageView)
                        } else {
                            Log.d("Movie Error", "Backdrop path not found for the selected movie")
                        }
                    } else {
                        Log.d("Movie Error", "No movies found in the response")
                    }
                } catch (e: JSONException) {
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

