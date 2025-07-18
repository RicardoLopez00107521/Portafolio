package com.avanzo.avanzotestsimulator.ui.ranking.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.databinding.RankingItemBinding

class RankingRecyclerViewAdapter(private val clickListener: (RankingModel) -> Unit) : RecyclerView.Adapter<RankingRecyclerViewHolder>() {
    private val rankings = ArrayList<RankingModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingRecyclerViewHolder {
        val binding = RankingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingRecyclerViewHolder, position: Int) {
        val ranking = rankings[position]
        holder.bind(ranking, clickListener,position)
    }

    override fun getItemCount(): Int {
        return rankings.size
    }

    fun setData(rankingList: List<RankingModel>) {
        rankings.clear()
        rankings.addAll(rankingList)
    }
}