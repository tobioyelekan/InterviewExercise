package com.example.interviewexercise.data

const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

//Model to be used in ui
data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val imgUrl: String,
    val isHighlight: Boolean
)

fun MovieResponse.toMovie(): Movie {
    return Movie(
        id = this.id ?: 0L,
        title = this.title ?: "",
        overview = this.overview ?: "",
        imgUrl = "$IMAGE_URL${this.posterPath ?: ""}",
        isHighlight = (voteAverage ?: 0.0) >= 7
    )
}