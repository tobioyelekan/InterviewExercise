package com.example.interviewexercise.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.interviewexercise.data.Movie
import com.example.interviewexercise.data.toMovie
import com.example.interviewexercise.networking.RetrofitFactory
import com.example.interviewexercise.views.gallery.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository {
    private val movieApi = RetrofitFactory.getMovieApi()

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieApi)
            },
            initialKey = 1
        ).flow
            .map { pagingData -> pagingData.map { it.toMovie() } }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
