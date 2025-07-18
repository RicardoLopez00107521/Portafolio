package com.avanzo.avanzotestsimulator.ui.results

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import kotlin.math.round
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.FragmentRankingBinding
import com.avanzo.avanzotestsimulator.databinding.FragmentResultsBinding
import com.avanzo.avanzotestsimulator.ui.results.viewmodel.ResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale


class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    private lateinit var monthPickerButton: Button
    private val resultViewModel: ResultViewModel by activityViewModels {
        ResultViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthPickerButton = view.findViewById(R.id.monthPickerButton)
        monthPickerButton.setOnClickListener {
            openMonthPickerDialog()
        }
    }

    private fun openMonthPickerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_month_picker, null)
        val monthPicker = dialogView.findViewById<NumberPicker>(R.id.monthPicker)
        val yearPicker = dialogView.findViewById<NumberPicker>(R.id.yearPicker)

        // Configurar el NumberPicker para mostrar los meses
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months

        // Configurar el NumberPicker para mostrar los años
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = 2015
        yearPicker.maxValue = 2030
        yearPicker.value = Math.max(currentYear, 2023) // Establecer el valor inicial en el máximo entre el año actual y 2023

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Select Month and Year")
            .setPositiveButton("Select") { _, _ ->
                val selectedMonth = monthPicker.value + 1 // Agregar 1 al índice del mes seleccionado
                val selectedYear = yearPicker.value
                val selectedDateText = String.format(Locale.getDefault(), "%d/%d", selectedMonth, selectedYear)
                monthPickerButton.text = selectedDateText

                CoroutineScope(Dispatchers.Main).launch {
                    resultViewModel.getNotesHistory()
                    checkExamResults(selectedYear, selectedMonth)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }


    @SuppressLint("SetTextI18n")
    private suspend fun checkExamResults(year: Int, month: Int) {
        val formattedYear = year.toFloat().toString()
        val formattedMonth = month.toFloat().toString()
        val notesByYear = resultViewModel.getNotesbyYear(formattedYear)
        val notesByMonth = resultViewModel.getNotesbyMonth(formattedMonth)

        if (notesByYear != null) {
            if (notesByMonth != null) {
                Log.d("resultfragment", "Obtuve: ${notesByMonth}")
                val monthPoints = (notesByMonth.toFloat() * 15).toInt().toString()
                Log.d("resultfragment", "Converti: ${monthPoints}")
                binding.pointsTV.text = monthPoints
                Log.d("ExamResults", "Notes: $monthPoints")
            } else {
                binding.pointsTV.text = "No hay resultados que mostrar"
                Log.d("ExamResults", "No se han realizado exámenes en este mes")
            }
        } else {
            binding.pointsTV.text = "No hay resultados que mostrar"
            Log.d("ExamResults", "No se han realizado exámenes en este año")
        }
    }
}








