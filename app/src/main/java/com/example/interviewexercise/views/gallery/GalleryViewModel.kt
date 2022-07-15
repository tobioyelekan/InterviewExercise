package com.example.interviewexercise.views.gallery

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.interviewexercise.data.Movie
import com.example.interviewexercise.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GalleryViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val allMovies: Flow<PagingData<Movie>> =
        movieRepository.getPopularMovies()
            .cachedIn(viewModelScope)
}
