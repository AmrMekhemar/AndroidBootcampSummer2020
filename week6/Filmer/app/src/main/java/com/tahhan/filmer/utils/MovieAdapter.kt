package com.tahhan.filmer.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tahhan.filmer.Constants
import com.tahhan.filmer.R
import com.tahhan.filmer.model.Movie
import com.tahhan.filmer.view.DetailsFragment


class MovieAdapter(movies: List<Movie>, val listener: (Int) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder?>() {
     var movieList: MutableList<Movie> = movies as MutableList<Movie>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ItemPoster: ImageView = itemView.findViewById(R.id.item_poster)
        var ItemTitle: TextView = itemView.findViewById(R.id.item_title)
        var viewHolderView: View

        init {
            viewHolderView = itemView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_menu_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieItem: Movie = movieList[position]
        holder.ItemTitle.text = movieItem.title
        val imagePath: String = Constants.ROOT_POSTER_IMAGE_URL + movieItem.poster_path
        Picasso.get()
            .load(imagePath)
            .placeholder(R.drawable.load)
            .error(R.color.divider)
            .into(holder.ItemPoster)

        holder.viewHolderView.setOnClickListener {
            listener(movieItem.id)
        }

    }


    override fun getItemCount(): Int {
        return movieList.size
    }


}