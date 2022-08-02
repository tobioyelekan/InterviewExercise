package com.example.interviewexercise

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import app.cash.turbine.test
import com.example.interviewexercise.MoviePagingSourceTest.Companion.sampleResponse
import com.example.interviewexercise.data.toMovie
import com.example.interviewexercise.repository.MovieRepository
import com.example.interviewexercise.views.gallery.GalleryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration
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
    fun `assert that when viewModel is launched, paging data starts collecting a stream of movies`() = runBlockingTest {
        viewModel.allMovies.test {
            assertEquals(
                sampleResponse.results!!.map { it.toMovie() },
                expectItem().collectDataForTest()
            )
        }
    }

    private suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }
}