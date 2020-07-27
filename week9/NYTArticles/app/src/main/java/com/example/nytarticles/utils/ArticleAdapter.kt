package com.example.nytarticles.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nytarticles.App
import com.example.nytarticles.R
import com.example.nytarticles.model.Article


class ArticleAdapter(private var articles: List<Article>, val listener: (Any) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder?>() {
    private val context = App.getAppContext()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var abstract: TextView = itemView.findViewById(R.id.abstractTitle)
        var isFavoriteImage: ImageView = itemView.findViewById(R.id.isFavImage)
        var image: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleItem = articles[position]
        holder.title.text = articleItem.title
        holder.abstract.text = articleItem.abstractTitle
        if (articleItem.isFavorite) holder.isFavoriteImage.setImageDrawable(
            context.getDrawable(R.drawable.heart_minus)
        )
        else holder.isFavoriteImage.setImageDrawable(
            context.getDrawable(R.drawable.heart_plus)
        )

        Glide
            .with(context)
            .load(articleItem.multimedia[0].url)
            .centerCrop()
            .into(holder.image)

        holder.isFavoriteImage.setOnClickListener {
            when (articleItem.isFavorite) {
                true -> {
                    holder.isFavoriteImage.setImageDrawable(
                        context.getDrawable(R.drawable.heart_plus)
                    )
                }
                false -> {
                    holder.isFavoriteImage.setImageDrawable(
                        context.getDrawable(R.drawable.heart_minus)
                    )
                }
            }
            listener(articleItem)
        }
        holder.itemView.setOnClickListener {
            listener(articleItem.url)
        }

    }


    override fun getItemCount(): Int {
        return articles.size
    }


}