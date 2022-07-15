package com.example.interviewexercise.views.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewexercise.data.Movie
import com.example.interviewexercise.databinding.MovieItemBinding
import com.example.interviewexercise.views.loadImage

class MovieAdapter(
    private val onMovieSelected: ((String) -> Unit)
) : PagingDataAdapter<Movie, MovieAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie == null) {
            super.bindViewHolder(holder, position)
        } else {
            holder.bind(movie, onMovieSelected)
        }
    }

    class ViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, onMovieSelected: (String) -> Unit) {
            with(binding) {
                title.text = movie.title
                overview.text = movie.overview
                img.loadImage(movie.imgUrl)
                rating.isVisible = movie.isHighlight

                itemView.setOnClickListener {
                    onMovieSelected(movie.title)
                }

                img.setOnClickListener {
                    onMovieSelected(movie.title)
                }
            }
        }
    }

    internal class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}