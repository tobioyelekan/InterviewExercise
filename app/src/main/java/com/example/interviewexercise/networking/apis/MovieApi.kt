package com.example.interviewexercise.networking.apis

import com.example.interviewexercise.data.PopularMoviesResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.example.interviewexercise.data.GenericErrorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMoviesAsync(
        @Query("page") page: Int
    ): NetworkResponse<PopularMoviesResponse, GenericErrorResponse>
}
