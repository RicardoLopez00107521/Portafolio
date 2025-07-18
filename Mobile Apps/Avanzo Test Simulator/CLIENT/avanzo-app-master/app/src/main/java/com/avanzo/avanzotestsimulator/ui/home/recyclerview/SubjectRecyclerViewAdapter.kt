package com.avanzo.avanzotestsimulator.ui.home.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.databinding.SubjectItemBinding

class SubjectRecyclerViewAdapter(private val clickListener: (SubjectModel) -> Unit) : RecyclerView.Adapter<SubjectRecyclerViewHolder>() {
    val subjects = ArrayList<SubjectModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectRecyclerViewHolder {
        val binding = SubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectRecyclerViewHolder, position: Int) {
        val subject = subjects[position]
        holder.bind(subject, clickListener)

    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    fun setData(subjectList: List<SubjectModel>) {
        subjects.clear()
        subjects.addAll(subjectList)
    }

}