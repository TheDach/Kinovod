package com.thedach.kinovod.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentMovieDetailBinding
import com.thedach.kinovod.presentation.adapters.ActorsAdapter
import com.thedach.kinovod.presentation.adapters.ReviewAdapter

class MovieDetailsFragment: Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieDetailBinding == null")

    private val args by navArgs<MovieDetailsFragmentArgs>()

    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private val viewModelFactory: MovieDetailsViewModelFactory by lazy {
        MovieDetailsViewModelFactory(
            args.movie.id
        )
    }

    private val viewModel: MovieDetailsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MovieDetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        bindViews()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnComeBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tabFriends.setOnClickListener {
            binding.tabFriends.setTextColor(
                resources.getColor(R.color.purple_500, null)
            )
            binding.tabKinopoisk.setTextColor(
                resources.getColor(R.color.tags_text_grey_poster, null)
            )
        }
        binding.tabKinopoisk.setOnClickListener {
            binding.tabKinopoisk.setTextColor(
                resources.getColor(R.color.purple_500, null)
            )
            binding.tabFriends.setTextColor(
                resources.getColor(R.color.tags_text_grey_poster, null)
            )
        }

        binding.btnWantWatch.setOnClickListener {
            viewModel.toggleWishList(args.movie.id)
        }
        binding.btnWatched.setOnClickListener {
            viewModel.toggleWatchedList(args.movie.id)
        }
    }

    private fun observeViewModel() {
        viewModel.reviewList.observe(viewLifecycleOwner) {reviews ->
            reviewAdapter.submitList(reviews)
        }

        viewModel.isInWishList.observe(viewLifecycleOwner) { isInWishList ->
            updateWishListButtonState(isInWishList)
        }

        viewModel.isInWatchedList.observe(viewLifecycleOwner) { isInWatchedList ->
            updateWatchedButtonState(isInWatchedList)
        }
    }

    private fun bindViews() {
        with(binding) {
            with(args) {

                Picasso.get().load(movie.poster).into(imageViewMovieImage)
                tvMovieTitle.text = movie.name

                chipRatingKinopoisk.text = resources.getString(R.string.tv_rating_kinopoisk)
                    .format(movie.rating.kp)
                chipRatingImdb.text = resources.getString(R.string.tv_rating_imdb)
                    .format(movie.rating.imdb)
                chipYearMovie.text = resources.getString(R.string.tv_year_movie)
                    .format(movie.year)

                tvTimeMovieDetail.text = resources.getString(R.string.tv_time_movie_detail)
                    .format(movie.movieLengthHour, movie.movieLengthMin)
                tvTagMovieDetail.text = resources.getString(R.string.tv_tag_movie_detail)
                    .format(movie.genres[0])
                tvAgeLimitMovieDetail.text = resources.getString(R.string.tv_age_limit_movie_detail)
                    .format(movie.ageRating)

                tvMovieDescription.text = movie.description

                setupRecyclerView()
            }
        }
    }

    private fun setupRecyclerView() {
        actorsAdapter = ActorsAdapter()
        binding.recyclerViewActors.adapter = actorsAdapter
        actorsAdapter.submitList(args.movie.persons)

        reviewAdapter = ReviewAdapter(requireContext())
        binding.recyclerViewReviews.adapter = reviewAdapter
    }

    private fun updateWishListButtonState(isInList: Boolean) {
        with(binding.btnWantWatch) {
            if (isInList) {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                text = "В списке желаемого ✓"
            } else {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                text = "Хочу посмотреть"
            }
        }
    }

    private fun updateWatchedButtonState(isInList: Boolean) {
        with(binding.btnWatched) {
            if (isInList) {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                text = "Просмотрено ✓"
            } else {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                text = "Просмотрено"
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}