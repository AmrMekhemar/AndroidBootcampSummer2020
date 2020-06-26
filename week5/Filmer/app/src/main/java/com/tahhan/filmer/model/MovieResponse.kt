package com.tahhan.filmer.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(

    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<Movie>
) : Parcelable