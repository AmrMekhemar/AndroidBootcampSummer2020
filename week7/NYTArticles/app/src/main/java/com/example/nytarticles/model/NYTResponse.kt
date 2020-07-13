package com.example.nytarticles.model

import kotlinx.serialization.Serializable

@Serializable
data class NYTResponse (
    val status : String,
    val copyright : String,
    val section : String,
    val last_updated : String,
    val num_results : Int,
    val results : List<Article>
)