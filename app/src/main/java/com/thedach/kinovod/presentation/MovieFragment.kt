package com.thedach.kinovod.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentMovieBinding
import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.presentation.adapters.MovieAdapter
import com.thedach.kinovod.presentation.adapters.MovieViewModel

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding: FragmentMovieBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieBinding == null")

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this)[MovieViewModel::class.java]
    }

    private lateinit var movieAdapter: MovieAdapter

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
        setupClickListeners()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.movieList.observe(viewLifecycleOwner) {
            movieAdapter.submitList(it)
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

    private fun launchMovieDetailsFragment(movie: Movie) {
        findNavController().navigate(R.id.action_movieFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}