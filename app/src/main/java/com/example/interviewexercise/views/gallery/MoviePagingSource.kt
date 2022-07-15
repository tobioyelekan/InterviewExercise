package com.example.interviewexercise.views.gallery

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.interviewexercise.data.MovieResponse
import com.example.interviewexercise.networking.apis.MovieApi
import com.haroldadmin.cnradapter.NetworkResponse

class MoviePagingSource(private val api: MovieApi) : PagingSource<Int, MovieResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponse> {
        val page = params.key ?: 1
        return when (val response = api.getPopularMoviesAsync(page)) {
            is NetworkResponse.Success -> LoadResult.Page(
                data = response.body.results ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
            is NetworkResponse.ServerError -> LoadResult.Error(Exception(response.body?.message))
            is NetworkResponse.NetworkError -> LoadResult.Error(response.error)
            is NetworkResponse.UnknownError -> LoadResult.Error(response.error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}