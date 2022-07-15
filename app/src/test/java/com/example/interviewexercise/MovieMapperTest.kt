package com.example.interviewexercise

import com.example.interviewexercise.MoviePagingSourceTest.Companion.sampleResponse
import com.example.interviewexercise.data.IMAGE_URL
import com.example.interviewexercise.data.Movie
import com.example.interviewexercise.data.toMovie
import junit.framework.Assert.assertEquals
import org.junit.Test

class MovieMapperTest {
    @Test
    fun `assert that mapping is correct`() {
        val uiMovies = sampleResponse.results!!.map { it.toMovie() }
        val singleMovie = sampleResponse.results!![0]
        val expected = Movie(
            id = singleMovie.id ?: 0L,
            title = singleMovie.title ?: "",
            overview = singleMovie.overview ?: "",
            imgUrl = "$IMAGE_URL${singleMovie.posterPath}",
            isHighlight = true
        )

        assertEquals(expected, uiMovies[0])
    }

    @Test
    fun `assert that when vote is not over 8, isHighlight is set to false`() {
        val uiMovies = sampleResponse.results!!.map { it.toMovie() }
        val singleMovie = sampleResponse.results!![1]
        val expected = Movie(
            id = singleMovie.id ?: 0L,
            title = singleMovie.title ?: "",
            overview = singleMovie.overview ?: "",
            imgUrl = "$IMAGE_URL${singleMovie.posterPath}",
            isHighlight = false
        )

        assertEquals(expected, uiMovies[1])
    }
}