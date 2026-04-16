package com.thedach.kinovod.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentMovieDetailBinding
import com.thedach.kinovod.presentation.adapters.ActorsAdapter
import com.thedach.kinovod.presentation.adapters.MovieAdapter
import kotlin.getValue

class MovieDetailsFragment: Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieDetailBinding == null")

    private val args by navArgs<MovieDetailsFragmentArgs>()

    private lateinit var actorsAdapter: ActorsAdapter


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
    }

    private fun setupClickListeners() {
        binding.btnComeBack.setOnClickListener {
            findNavController().popBackStack()
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}