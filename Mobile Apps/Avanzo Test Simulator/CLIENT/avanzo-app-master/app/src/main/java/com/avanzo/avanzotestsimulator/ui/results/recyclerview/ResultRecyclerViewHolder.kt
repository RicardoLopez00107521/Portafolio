package com.avanzo.avanzotestsimulator.ui.results.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.databinding.ResultItemBinding

class ResultRecyclerViewHolder(private val binding: ResultItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(subject: SubjectModel, clickListener: (SubjectModel) -> Unit) {
        binding.subjectTitle.text = subject.name

        binding.card.setOnClickListener {
            clickListener(subject)
        }
    }
}