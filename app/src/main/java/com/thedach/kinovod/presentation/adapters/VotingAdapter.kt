package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemVotingCardBinding
import com.thedach.kinovod.domain.model.room.SuggestionWithMovie
import com.thedach.kinovod.domain.model.room.VotingType

class VotingAdapter(
    private val context: Context
) : ListAdapter<SuggestionWithMovie, VotingViewHolder>(VotingItemDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VotingViewHolder {
        val binding = ItemVotingCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VotingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VotingViewHolder,
        position: Int
    ) {
        val voting = getItem(position).suggestion
        val movie = getItem(position).movie
        val progressPercent = getItem(position).progressPercent

        with(holder.binding) {
            tvVoteMovieTitle.text = movie.name
            Picasso.get().load(movie.poster).into(imageViewVotePoster)

            tvVoteMovieRating.text = context.getString(R.string.rating_star_poster)
                .format(movie.rating.kp)

            tvVoteMovieDuration.text = context.getString(R.string.tv_time_movie_detail)
                .format(movie.movieLengthHour, movie.movieLengthMin)

            tvVoteMovieYear.text = movie.year.toString()

            tvVoteCount.text = context.getString(R.string.tv_voting_count)
                .format(voting.voters.size.toString())


            tvVoteCount.text = voting.voters.size.toString()
            tvVotePercentage.text = "%s%%".format(progressPercent)
            progressBarVote.setProgress(progressPercent, true)

            if(getItem(position).votingAnonymous) {
                layoutVotersUsers.visibility = View.GONE
            }

//            when(getItem(position).votingType){
//                VotingType.SINGLE -> // нужно сделать
//                VotingType.MULTIPLE -> // нужно сделать
//                VotingType.PRIORITY -> // нужно сделать
//            }
        }

    }
}