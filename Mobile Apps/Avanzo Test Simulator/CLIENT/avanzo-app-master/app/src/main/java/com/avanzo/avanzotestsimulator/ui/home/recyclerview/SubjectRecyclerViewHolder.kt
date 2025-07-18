package com.avanzo.avanzotestsimulator.ui.home.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.database.model.SubjectModel

import com.avanzo.avanzotestsimulator.databinding.SubjectItemBinding

class SubjectRecyclerViewHolder(private val binding: SubjectItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(subject: SubjectModel, clickListener: (SubjectModel) -> Unit) {
        binding.subjectTitle.text = subject.name
        binding.subjectIcon.setImageResource(getDrawableResource(subject.name))

        binding.cardView.setOnClickListener {
            clickListener(subject)
        }
    }
    private fun getDrawableResource(subjectName: String): Int {
        val drawableResource: Int = when (subjectName) {
            "Matemática" -> R.drawable.math_icon
            "Inglés" -> R.drawable.english_icon
            "Ciencias" -> R.drawable.science_icon
            "Estudios Sociales" -> R.drawable.socials_icon
            "Lenguaje" -> R.drawable.language_icon
            // Add more cases for other subjects if needed
            else -> R.drawable.default_icon
        }
        return drawableResource
    }

}