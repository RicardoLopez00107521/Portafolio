package com.avanzo.avanzotestsimulator.ui.results.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.databinding.ResultItemBinding

class ResultRecyclerViewAdapter(private val clickListener: (SubjectModel) -> Unit) : RecyclerView.Adapter<ResultRecyclerViewHolder>() {
    val subjects = ArrayList<SubjectModel>()
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRecyclerViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultRecyclerViewHolder, position: Int) {
        val subject = subjects[position]
        holder.bind(subject, clickListener)
        holder.itemView.foreground = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_black)

        // Establecer el foreground según la posición seleccionada
        if (position == selectedPosition) {
            holder.itemView.foreground = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_blue)
        } else {
            holder.itemView.foreground = ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_black)
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    fun setData(subjectList: List<SubjectModel>) {
        subjects.clear()
        subjects.addAll(subjectList)
    }


}