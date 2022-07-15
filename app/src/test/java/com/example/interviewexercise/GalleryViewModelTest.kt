package com.example.interviewexercise

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.interviewexercise.MoviePagingSourceTest.Companion.sampleResponse
import com.example.interviewexercise.data.toMovie
import com.example.interviewexercise.repository.MovieRepository
import com.example.interviewexercise.views.gallery.GalleryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class GalleryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = mockk<MovieRepository>()
    private lateinit var viewModel: GalleryViewModel

    @Before
    fun setup() {
        coEvery { repository.getPopularMovies() } returns
                flowOf(PagingData.from(sampleResponse.results!!.map { it.toMovie() }))
        viewModel = GalleryViewModel(repository)
    }

    @Test
    fun `assert that when viewModel is launched and network publishes`() = runBlockingTest {
        viewModel.allMovies.test {
            assertEquals(
                PagingData.from(sampleResponse.results!!.map { it.toMovie() }),
                expectItem()
            )
        }
    }
}