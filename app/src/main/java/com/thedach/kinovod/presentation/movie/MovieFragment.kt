package com.thedach.kinovod.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentMovieBinding
import com.thedach.kinovod.domain.model.movie.Movie
import com.thedach.kinovod.domain.model.movie.SearchMode
import com.thedach.kinovod.presentation.adapters.MovieAdapter

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding: FragmentMovieBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieBinding == null")

    private val movieArgs by navArgs<MovieFragmentArgs>()
    private val suggestionMovieIds = mutableListOf<Int>()

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this)[MovieViewModel::class.java]
    }

    private lateinit var movieAdapter: MovieAdapter
    private val chipGroupFilters = mutableListOf<Chip>()

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
        setupMovieConfig()

        setupSwipeRefresh()
        setupSearchView()
        setupFiltersChipGroup()

        setupClickListeners()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.filteredMovieList.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies)

            if (movies.isEmpty() && viewModel.movieList.value?.isNotEmpty() == true) {
                Toast.makeText(requireContext(), "Ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarMovie.visibility = View.VISIBLE
            } else {
                binding.progressBarMovie.visibility = View.GONE
            }
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefresh ->
            if (isRefresh) {
                binding.swipeRefreshLayoutMovie.isRefreshing = true
            } else {
                binding.swipeRefreshLayoutMovie.isRefreshing = false
            }
        }
        viewModel.isSendingSuggestion.observe(viewLifecycleOwner) {isSending ->
            if (isSending) {
                binding.buttonConfirmMovie.text = getString(R.string.btn_sending)
            } else {
                binding.buttonConfirmMovie.text = getString(R.string.btn_submit)
                launchVotingRoomFragment()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMovieConfig() {
        when(movieArgs.movieSelectionConfig.mode){
            SearchMode.ALL_MOVIES -> {
                // Ничего не делаем, init уже загрузил
            }
            SearchMode.ROOM_SUGGESTION -> {
                val genres = movieArgs.movieSelectionConfig.movieFilters?.genres

                binding.chipGroupFilters.removeAllViews()
                binding.buttonConfirmMovie.visibility = View.VISIBLE

                viewModel.setupExcludedMovies(movieArgs.movieSelectionConfig.movieFilters?.suggestions)

                movieAdapter.setupMovieSelectionConfig(movieArgs.movieSelectionConfig)

                if (!genres.isNullOrEmpty()) {
                    genres.forEach {genre ->
                        viewModel.addActiveChip(genre)
                    }

                    viewModel.loadMoviesByGenres(genres)
                } //else {
//                    viewModel.refreshMovies()
//                    Вроде как это излишне так как при создании viewModel сразу грузит фильмы и если Genres==null то ничего и не надо грузить по новой
//                }
            }
        }

    }

    private fun setupClickListeners() {
        binding.buttonSettings.setOnClickListener {
            showGenresDialog()
        }
        binding.buttonConfirmMovie.setOnClickListener {
            viewModel.suggestMovie(
                movieArgs.movieSelectionConfig.roomId
                    ?: throw Exception("Error: Почему в MovieFragment прилетел roomId == null???"),
                suggestionMovieIds
            )
        }
        movieAdapter.onMovieClickListener = { movie ->
            launchMovieDetailsFragment(movie)
        }
        movieAdapter.onAddSuggestionClickListener = { movieIds ->
            suggestionMovieIds.add(movieIds)
            movieAdapter.updateSuggestedMovies(suggestionMovieIds)
        }
        movieAdapter.onRemoveSuggestionClickListener = {movieIds ->
            suggestionMovieIds.remove(movieIds)
            movieAdapter.updateSuggestedMovies(suggestionMovieIds)
        }
    }

    private fun setupSearchView() {
        binding.searchViewMovie.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                viewModel.searchMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                viewModel.searchMovies(newText)
                return true
            }
        })

        binding.searchViewMovie.setOnCloseListener {
            viewModel.clearSearch()
            false
        }
    }

    private fun setupFiltersChipGroup() {
        binding.chipGroupFilters.removeAllViews()

        viewModel.getActiveChips().forEach { genre ->
            addFilterChip(genre)
        }
    }

    private fun addFilterChip(genre: String) {
        val chip = layoutInflater.inflate(
            R.layout.item_chip_genre,
            binding.chipGroupFilters,
            false
        ) as Chip
        chip.text = genre

        when(movieArgs.movieSelectionConfig.mode) {

            SearchMode.ALL_MOVIES -> {
                chip.isCloseIconVisible = true

                chip.setOnCloseIconClickListener {

                    binding.chipGroupFilters.removeView(chip)
                    viewModel.removeActiveChip(genre)

                    val selectedGenres = binding.chipGroupFilters.children
                        .filterIsInstance<Chip>()
                        .map { it.text.toString() }
                        .toList()

                    viewModel.loadMoviesByGenres(selectedGenres)
                }
            }

            SearchMode.ROOM_SUGGESTION -> {
                chip.isCloseIconVisible = false
            }
        }

        binding.chipGroupFilters.addView(chip)
        chipGroupFilters.add(chip)
    }

    private fun showGenresDialog() {
        val dialog = GenresDialogFragment(
            selectedGenres = viewModel.getActiveChips(),
            onApply = { selectedGenres ->

                binding.chipGroupFilters.removeAllViews()
                chipGroupFilters.clear()

                selectedGenres.forEach { genre ->
                    addFilterChip(genre)
                    viewModel.addActiveChip(genre)
                }

                viewModel.loadMoviesByGenres(selectedGenres)
            }
        )
        dialog.show(childFragmentManager, "GenresDialog")
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(requireContext())
        binding.recyclerViewMovie.adapter = movieAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayoutMovie.setOnRefreshListener {
            viewModel.refreshMovies()
            viewModel.clearSearch()
            binding.searchViewMovie.setQuery("", false)
            binding.searchViewMovie.clearFocus()
        }
    }

    private fun launchMovieDetailsFragment(movie: Movie) {
        findNavController().navigate(
            MovieFragmentDirections.actionMovieFragmentToMovieDetailsFragment(movie)
        )
    }

    private fun launchVotingRoomFragment() {
        findNavController().navigate(
            MovieFragmentDirections.actionMovieFragmentToVotingRoomFragment(
                movieArgs.movieSelectionConfig.roomId
                    ?: throw Exception("Error: Почему в MovieFragment прилетел roomId == null???")
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}