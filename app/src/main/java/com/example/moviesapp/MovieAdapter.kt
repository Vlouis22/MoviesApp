package com.example.moviesapp

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val movieList: List<String>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieText: TextView
        val movieImage: ImageView

        init {
            movieText = view.findViewById(R.id.movieTitleText)
            movieImage = view.findViewById(R.id.movie_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieInfo = movieList[position].split("\n")
        val title = movieInfo[0]
        val releaseDate = movieInfo[1]
        val rating = movieInfo[2]

        holder.movieText.text = title
        Glide.with(holder.itemView)
            .load(movieInfo) // Load your movie poster image here
            .centerCrop()
            .into(holder.movieImage)
    }

    override fun getItemCount() = movieList.size
}
