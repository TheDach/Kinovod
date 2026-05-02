package com.thedach.kinovod.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.thedach.kinovod.databinding.ItemActorBinding
import com.thedach.kinovod.domain.model.profile.Person

class ActorsAdapter : ListAdapter<Person, ActorsViewHolder>(ActorsItemDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorsViewHolder {
        val binding = ItemActorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActorsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActorsViewHolder,
        position: Int
    ) {
        val person = getItem(position)

        with(holder.binding){
            tvActorName.text = person.name
            Picasso.get().load(person.photo).into(imageViewActorImage)
        }
    }
}