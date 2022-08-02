package com.example.interviewexercise

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.example.interviewexercise.data.MovieResponse
import com.example.interviewexercise.data.PopularMoviesResponse
import com.example.interviewexercise.networking.apis.MovieApi
import com.example.interviewexercise.views.gallery.MoviePagingSource
import com.haroldadmin.cnradapter.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MoviePagingSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val api = mockk<MovieApi>()
    private lateinit var moviePagingSource: MoviePagingSource

    @Before
    fun setup() {
        moviePagingSource = MoviePagingSource(api)
    }

    @Test
    fun `assert that when network call is successful and the first page is loaded, prevKey would be null and nextKey is incremented to 2`() = runBlockingTest {
        coEvery { api.getPopularMoviesAsync(any()) } returns NetworkResponse.Success(
            body = sampleResponse, null, 200
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = sampleResponse.results!!.toList(),
            prevKey = null,
            nextKey = 2
        )
        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `assert that when network call is successful and the second page is loaded, prevKey would be 1 and nextKey is incremented to 3`() = runBlockingTest {
        coEvery { api.getPopularMoviesAsync(any()) } returns NetworkResponse.Success(
            body = sampleResponse, null, 200
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = sampleResponse.results!!.toList(),
            prevKey = 1,
            nextKey = 3
        )
        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `assert that when current page is 2 and attempt to load first page is successful, prevKey would be null and next key would be 2`() = runBlockingTest {
        coEvery { api.getPopularMoviesAsync(any()) } returns NetworkResponse.Success(
            body = sampleResponse, null, 200
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = sampleResponse.results!!.toList(),
            prevKey = null,
            nextKey = 2
        )
        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `assert that when current page is 3 and attempt to load page 2 is successful, prevKey would be 1 and next key would be 3`() = runBlockingTest {
        coEvery { api.getPopularMoviesAsync(any()) } returns NetworkResponse.Success(
            body = sampleResponse, null, 200
        )
        val expectedResult = PagingSource.LoadResult.Page(
            data = sampleResponse.results!!.toList(),
            prevKey = 1,
            nextKey = 3
        )
        assertEquals(
            expectedResult, moviePagingSource.load(
                PagingSource.LoadParams.Prepend(
                    key = 2,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun loadReturnsPageWhenFailed() = runBlockingTest {
        coEvery { api.getPopularMoviesAsync(any()) } returns NetworkResponse.NetworkError(
            IOException("no internet")
        )
        val expectedResult =
            PagingSource.LoadResult.Error<Any, Any>(IOException("no internet"))

        val actualResult = moviePagingSource.load(
            PagingSource.LoadParams.Append(
                key = 1,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertEquals(expectedResult.throwable.message, (actualResult as PagingSource.LoadResult.Error).throwable.message)
    }

    companion object {
        val sampleResponse = PopularMoviesResponse(
            1, arrayListOf(
                MovieResponse(
                    id = 1L,
                    title = "X-Men",
                    overview = "X-Men is an American superhero film series based on the Marvel Comics",
                    posterPath = "\"/ta17TltHGdZ5PZz6oUD3N5BRurb.jpg\"",
                    voteAverage = 7.6,
                    voteCount = 1098,
                    releaseDate = "2020-02-18",
                    genres = null
                ),
                MovieResponse(
                    id = 2L,
                    title = "X-Men",
                    overview = "X-Men is an American superhero film series based on the Marvel Comics",
                    posterPath = "\"/ta17TltHGdZ5PZz6oUD3N5BRurb.jpg\"",
                    voteAverage = 6.6,
                    voteCount = 1098,
                    releaseDate = "2020-02-18",
                    genres = null
                )
            )
        )
    }
}