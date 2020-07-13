package com.example.nytarticles.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "Articles")
@Serializable
data class Article(
    var section: String = "",
    var subsection: String = "",
    var title: String = "",
    var url: String = "",
    var uri: String = "",
    var byline: String = "",
    var item_type: String = "",
    var updated_date: String = "",
    var created_date: String = "",
    var published_date: String = "",
    @PrimaryKey
    @SerialName("abstract")
    var abstractTitle: String = "",
    var multimedia: List<Multimedia> = listOf(),
    var short_url: String = "",
    var isFavorite : Boolean = false
)