package com.example.nytarticles.model


data class NYTResponse (
    val status : String,
    val copyright : String,
    val section : String,
    val last_updated : String,
    val num_results : Int,
    val results : List<Article>
)