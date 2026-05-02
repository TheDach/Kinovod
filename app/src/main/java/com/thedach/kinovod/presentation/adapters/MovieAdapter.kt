package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemMovieBinding
import com.thedach.kinovod.domain.model.movie.Movie

class MovieAdapter(
    private val context: Context
) : ListAdapter<Movie, MovieViewHolder>(MovieItemDiffCallback) {

    var onMovieClickListener: ((Movie) -> Unit)? = null

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

                tvRatingKinopoisk.text = context.resources.getString(R.string.rating_poster_kinopoinsk_poster)
                    .format(rating.kp)
                tvRatingImdb.text = context.resources.getString(R.string.rating_poster_IMDb_poster)
                    .format(rating.imdb)
                tvRatingStarPoster.text = context.resources.getString(R.string.rating_star_poster)
                    .format(rating.kp)

                Picasso.get().load(poster).into(imageViewMoviePoster)

                root.setOnClickListener {
                    onMovieClickListener?.invoke(this)
                }
            }
        }
    }
}