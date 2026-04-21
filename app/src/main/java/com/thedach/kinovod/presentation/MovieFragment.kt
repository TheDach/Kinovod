package com.thedach.kinovod.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thedach.kinovod.databinding.FragmentMovieBinding
import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.presentation.adapters.MovieAdapter

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding: FragmentMovieBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieBinding == null")

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this)[MovieViewModel::class.java]
    }

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()

        setupClickListeners()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.movieList.observe(viewLifecycleOwner) {movies ->
            movieAdapter.submitList(movies)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if(isLoading) {
                binding.progressBarMovie.visibility = View.VISIBLE
            } else {
                binding.progressBarMovie.visibility = View.GONE
            }
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) {isRefresh ->
            if (isRefresh) {
                binding.swipeRefreshLayoutMovie.isRefreshing = true
            } else {
                binding.swipeRefreshLayoutMovie.isRefreshing = false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setupClickListeners() {
        binding.buttonSettings.setOnClickListener {
            TODO()
        }
        movieAdapter.onMovieClickListener = { movie ->
            launchMovieDetailsFragment(movie)
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(requireContext())
        binding.recyclerViewMovie.adapter = movieAdapter
    }

    private fun setupSwipeRefresh(){
        binding.swipeRefreshLayoutMovie.setOnRefreshListener {
            viewModel.refreshMovies()
        }
    }

    private fun launchMovieDetailsFragment(movie: Movie) {
        findNavController().navigate(
            MovieFragmentDirections.actionMovieFragmentToMovieDetailsFragment(movie)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}