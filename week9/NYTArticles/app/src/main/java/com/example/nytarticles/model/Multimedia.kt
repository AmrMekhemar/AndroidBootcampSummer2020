package com.example.nytarticles.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Multimedia(
    @PrimaryKey
    var url: String = "",
    var format: String = "",
    var height: Int = 0,
    var width: Int = 0,
    var type: String = "",
    var subtype: String = "",
    var caption: String = "",
    var copyright: String = ""
)