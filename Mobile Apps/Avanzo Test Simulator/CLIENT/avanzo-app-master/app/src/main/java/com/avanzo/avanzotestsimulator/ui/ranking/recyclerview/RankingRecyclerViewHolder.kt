package com.avanzo.avanzotestsimulator.ui.ranking.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.databinding.RankingItemBinding

class RankingRecyclerViewHolder(private val binding: RankingItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(ranking: RankingModel, clickListener: (RankingModel) -> Unit,position: Int) {
        binding.userName.text = ranking.name
        binding.numberPoints.text = ranking.points.toString()
        binding.positionText.text = (position+1).toString()

        val userInitial = ranking.name?.getOrNull(0)?.toString()?.toUpperCase() ?: ""
        binding.userInitial?.text = userInitial

        binding.card.setOnClickListener {
            clickListener(ranking)
        }
    }

}