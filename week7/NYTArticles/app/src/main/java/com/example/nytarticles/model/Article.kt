package com.example.nytarticles.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Articles")
data class Article(
    var section: String = "",
    var subsection: String = "",
    var title: String = "",
    @SerializedName("abstract")
    @PrimaryKey
    var abstractTitle: String = "",
    var url: String = "",
    var uri: String = "",
    var byline: String = "",
    var item_type: String = "",
    var updated_date: String = "",
    var created_date: String = "",
    var published_date: String = "",
    var material_type_facet: String = "",
    var multimedia: List<Multimedia> = listOf(),
    var short_url: String = "",
    var isFavorite: Boolean = false




)