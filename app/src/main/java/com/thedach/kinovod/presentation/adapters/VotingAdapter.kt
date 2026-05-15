package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.databinding.ItemVotingCardBinding
import com.thedach.kinovod.domain.model.room.SuggestionWithMovie
import com.thedach.kinovod.domain.model.room.VotingType

class VotingAdapter(
    private val context: Context,
    private val onVoteClick: (suggestionId: Int, movieId: Int, isSelected: Boolean) -> Unit
) : ListAdapter<SuggestionWithMovie, VotingViewHolder>(VotingItemDiffCallback) {

    private val selectedVotes = mutableSetOf<Pair<Int, Int>>() // (suggestionId, movieId)
    private var singleSelectedVote: Pair<Int, Int>? = null

    private var votingType: VotingType? = null

    fun getSelectedMovieIds(): List<Int> {
        return when (votingType) {
            VotingType.SINGLE -> listOfNotNull(singleSelectedVote?.second)
            VotingType.MULTIPLE -> selectedVotes.map { it.second }
            VotingType.PRIORITY -> emptyList()
            null -> emptyList()
        }
    }


    fun getSelectedSuggestionIds(): List<Int> {
        return when (votingType) {
            VotingType.SINGLE -> listOfNotNull(singleSelectedVote?.first)
            VotingType.MULTIPLE -> selectedVotes.map { it.first }
            VotingType.PRIORITY -> emptyList()
            null -> emptyList()
        }
    }

    // Очистить выборы
    fun clearSelections() {
        selectedVotes.clear()
        singleSelectedVote = null
        notifyDataSetChanged()
    }

    // Проверить, голосовал ли пользователь за конкретный suggestion
    fun hasUserVoted(suggestionId: Int): Boolean {
        return currentList.find { it.suggestion.suggestionId == suggestionId }
            ?.suggestion?.voters?.contains(UserRepository.getUserId()) == true
    }

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
        val hasUserVoted = hasUserVoted(voting.suggestionId)
        votingType = getItem(position).votingType

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

            if (getItem(position).votingAnonymous) {
                layoutVotersUsers.visibility = View.GONE
            }

            if (votingType != null) {
                setupVotingUI(getItem(position), hasUserVoted, holder)
            }

        }

    }

    private fun setupVotingUI(
        item: SuggestionWithMovie,
        hasUserVoted: Boolean,
        holder: VotingViewHolder
    ) {
        val suggestionId = item.suggestion.suggestionId
        val movieId = item.suggestion.movieId

        with(holder.binding) {
            when (votingType) {
                VotingType.SINGLE -> {
                    radioButtonVote.visibility = View.VISIBLE
                    checkBoxVote.visibility = View.GONE

                    // Проверяем, голосовал ли пользователь за ЛЮБОЙ фильм в этом голосовании
                    val hasVotedInThisSession = currentList.any { suggestionWithMovie ->
                        suggestionWithMovie.suggestion.voters.contains(UserRepository.getUserId())
                    }

                    if (hasVotedInThisSession) {
                        // Если пользователь уже голосовал в этом голосовании
                        if (hasUserVoted) {
                            // Показываем, что он голосовал именно за этот фильм
                            radioButtonVote.isChecked = true
                            radioButtonVote.isEnabled = false
                            radioButtonVote.text = "Вы проголосовали"
                        } else {
                            // За этот фильм не голосовал, но голосовал за другой - блокируем
                            radioButtonVote.isChecked = false
                            radioButtonVote.isEnabled = false
                            radioButtonVote.text = "Голосование завершено"
                        }
                    } else {
                        // Пользователь ещё не голосовал - разрешаем выбор
                        radioButtonVote.isChecked = (singleSelectedVote?.first == suggestionId)
                        radioButtonVote.isEnabled = true
                        radioButtonVote.text = "Голосовать"

                        radioButtonVote.setOnClickListener {
                            val previousSelected = singleSelectedVote
                            singleSelectedVote = Pair(suggestionId, movieId)

                            // Обновляем предыдущий выбранный элемент
                            previousSelected?.let { (oldId, _) ->
                                val oldPosition = currentList.indexOfFirst {
                                    it.suggestion.suggestionId == oldId
                                }
                                if (oldPosition != -1) {
                                    notifyItemChanged(oldPosition)
                                }
                            }

                            onVoteClick(suggestionId, movieId, true)
                            notifyItemChanged(holder.adapterPosition)
                        }
                    }
                }


                VotingType.MULTIPLE -> {
                    radioButtonVote.visibility = View.GONE
                    checkBoxVote.visibility = View.VISIBLE

                    if (hasUserVoted) {
                        checkBoxVote.isChecked = true
                        checkBoxVote.isEnabled = false
                        checkBoxVote.text = "Вы проголосовали"
                    } else {
                        checkBoxVote.isChecked = selectedVotes.any { it.first == suggestionId }
                        checkBoxVote.isEnabled = true
                        checkBoxVote.text = "Голосовать"

                        checkBoxVote.setOnClickListener {
                            if (!hasUserVoted) {
                                if (checkBoxVote.isChecked) {
                                    selectedVotes.add(Pair(suggestionId, movieId))
                                    onVoteClick(suggestionId, movieId, true)
                                } else {
                                    selectedVotes.remove(Pair(suggestionId, movieId))
                                    onVoteClick(suggestionId, movieId, false)
                                }
                            }
                        }
                    }
                }

                VotingType.PRIORITY -> {

                    radioButtonVote.visibility = View.GONE
                    checkBoxVote.visibility = View.GONE
                }

                null -> {}
            }
        }

    }
}