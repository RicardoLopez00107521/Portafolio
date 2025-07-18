package com.avanzo.avanzotestsimulator.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.databinding.FragmentRankingBinding
import com.avanzo.avanzotestsimulator.ui.ranking.recyclerview.RankingRecyclerViewAdapter
import com.avanzo.avanzotestsimulator.ui.ranking.viewmodel.RankingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RankingFragment : Fragment() {

    private val rankingViewModel: RankingViewModel by activityViewModels {
        RankingViewModel.Factory
    }

    private lateinit var binding: FragmentRankingBinding
    private lateinit var adapter: RankingRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                //rankingViewModel.getRankingRemote()
                setRecyclerView(view)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funciones para implementar el RecyclerView

    private fun showSelectedItem(ranking: RankingModel) {
        rankingViewModel.setSelectedRanking(ranking) // Realiza la request a la API de los rankings
    }

    private suspend fun displayRankings() {
        adapter.setData(rankingViewModel.getRankings())
        adapter.notifyDataSetChanged()
    }

    private suspend fun setRecyclerView(view: View) {
        binding.RecyclerView.layoutManager = LinearLayoutManager(view.context)

        adapter = RankingRecyclerViewAdapter { selectedRanking ->
            showSelectedItem(selectedRanking)
        }

        binding.RecyclerView.adapter = adapter

        // Mostrar ProgressBar y ocultar RecyclerView
        binding.progressBar.visibility = View.VISIBLE
        binding.RecyclerView.visibility = View.GONE

        displayRankings()

        // Ocultar ProgressBar y mostrar RecyclerView
        binding.progressBar.visibility = View.GONE
        binding.RecyclerView.visibility = View.VISIBLE

        binding.RecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }
}