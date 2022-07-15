package com.example.interviewexercise.views.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.interviewexercise.databinding.FragmentGalleryBinding
import com.example.interviewexercise.repository.MovieRepository
import com.example.interviewexercise.views.ViewModelProviderFactory
import com.example.interviewexercise.views.showMessage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var moviesAdapter: MovieAdapter
    private lateinit var viewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ViewModelProviderFactory(GalleryViewModel(MovieRepository()))
        )[GalleryViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupClickListener()
    }

    private fun setupRecyclerView() {
        with(binding) {
            moviesAdapter = MovieAdapter {
                root.showMessage(it)
            }
            movieList.apply {
                addItemDecoration(GridSpacingItemDecoration(2, 40, true))
                adapter = moviesAdapter
                    .withLoadStateFooter(
                        footer = LoadingStateAdapter { moviesAdapter.retry() }
                    )
            }

            moviesAdapter.addLoadStateListener { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && moviesAdapter.itemCount == 0

                emptyState.isVisible = isListEmpty
                movieList.isVisible = !isListEmpty
                loading.isVisible = loadState.source.refresh is LoadState.Loading
                retry.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    root.showMessage(it.error.toString())
                }
            }
        }
    }

    private fun setupClickListener() {
        binding.retry.setOnClickListener {
            moviesAdapter.retry()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allMovies.collect {
                    moviesAdapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
