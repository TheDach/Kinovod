package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemMovieBinding
import com.thedach.kinovod.domain.model.movie.Movie
import com.thedach.kinovod.domain.model.movie.MovieSelectionConfig
import com.thedach.kinovod.domain.model.movie.SearchMode

class MovieAdapter(
    private val context: Context
) : ListAdapter<Movie, MovieViewHolder>(MovieItemDiffCallback) {

    var onMovieClickListener: ((Movie) -> Unit)? = null
    var onAddSuggestionClickListener: ((movieId: Int) -> Unit)? = null
    var onRemoveSuggestionClickListener: ((movieId: Int) -> Unit)? = null

    private var searchMode: SearchMode = SearchMode.ALL_MOVIES
    private val suggestedMovieIds = mutableSetOf<Int>()

    fun setupMovieSelectionConfig(config: MovieSelectionConfig) {
        searchMode = config.mode
    }

    fun updateSuggestedMovies(movieIds: List<Int>) {
        suggestedMovieIds.clear()
        suggestedMovieIds.addAll(movieIds)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        val movie = getItem(position)

        with(holder.binding) {
            with(movie) {
                tvMovieName.text = name
                tvMovieTag.text = genres[0]
                tvMovieTime.text = context.resources.getString(R.string.tag_and_time_poster)
                    .format(movieLengthHour, movieLengthMin)

                tvRatingKinopoisk.text =
                    context.resources.getString(R.string.rating_poster_kinopoinsk_poster)
                        .format(rating.kp)
                tvRatingImdb.text = context.resources.getString(R.string.rating_poster_IMDb_poster)
                    .format(rating.imdb)
                tvRatingStarPoster.text = context.resources.getString(R.string.rating_star_poster)
                    .format(rating.kp)

                Picasso.get().load(poster).into(imageViewMoviePoster)

                root.setOnClickListener {
                    onMovieClickListener?.invoke(this)
                }

                // Config
                if (searchMode == SearchMode.ROOM_SUGGESTION) {
                    btnAddMovieToSuggestion.visibility = View.VISIBLE

                    val isSuggested = suggestedMovieIds.contains(this.id)
                    updateButtonState(btnAddMovieToSuggestion, isSuggested)

                    btnAddMovieToSuggestion.setOnClickListener {
                        if (isSuggested) {
                            onRemoveSuggestionClickListener?.invoke(this.id)
                        } else {
                            onAddSuggestionClickListener?.invoke(this.id)
                        }
                        updateButtonState(btnAddMovieToSuggestion, isSuggested)
                    }


                } else {
                    btnAddMovieToSuggestion.visibility = View.GONE
                }
            }
        }
    }

    private fun updateButtonState(button: MaterialButton, isSuggested: Boolean) {
        if (isSuggested) {
            button.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                text = "Добавлено"
                icon = null
                setTextColor(ContextCompat.getColor(context, R.color.black))
                strokeWidth = 1
                strokeColor = ContextCompat.getColorStateList(context, R.color.black)
            }
        } else {
            button.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
                text = context.getString(R.string.btn_suggest_movie)
                setTextColor(ContextCompat.getColor(context, R.color.white))
                icon = ContextCompat.getDrawable(context, R.drawable.ic_plus_svg)
            }
        }
    }
}