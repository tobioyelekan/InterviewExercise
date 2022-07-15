package com.example.interviewexercise.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val results: ArrayList<MovieResponse>? = null
) : Parcelable