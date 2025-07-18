package com.avanzo.avanzotestsimulator.ui.home
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.databinding.FragmentHomeBinding
import com.avanzo.avanzotestsimulator.ui.home.recyclerview.SubjectRecyclerViewAdapter
import com.avanzo.avanzotestsimulator.ui.home.viewmodel.HomeViewModel
import com.avanzo.avanzotestsimulator.ui.home.viewmodel.UserViewModel
import com.avanzo.avanzotestsimulator.ui.tests.viewmodel.TestViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.Factory
    }

    private val testViewModel: TestViewModel by activityViewModels {
        TestViewModel.Factory
    }

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModel.Factory
    }

    val app by lazy {
        requireActivity().application as AvanzoReviewerApplication
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: SubjectRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("LALALALALA: ${app.getToken()}")

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                setRecyclerView(view)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }

        /*CoroutineScope(Dispatchers.Main).launch {
            try {
                userViewModel.getUsers()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }*/

        binding.userButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    userViewModel.getUsers()
                    val intent = Intent(requireContext(), ProfileActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    requireActivity().finish()

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Funciones para implementar el RecyclerView
    private fun showSelectedItem(subject: SubjectModel) {
        homeViewModel.setSelectedSubject(subject)

        val intent = Intent(requireContext(), TestDetailsActivity::class.java).apply {
            putExtra("subjectID", subject.subjectId)
            putExtra("subjectNAME", subject.name)
            putExtra("subjectCode", subject.subjectCode)
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("HomeFragment", "Queri obtener las preguntas de: ${subject.name}")
                // setRecyclerView(view)
                testViewModel.getQuestions(subject.name)

                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                requireActivity().finish()
            } catch (e: Exception) {
                Log.d("HomeFragment", "Mori ${subject.name}")
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show() // Cambié 'requireContext()' por 'this@TestDetailsActivity'
            }
        }
    }

    private suspend fun displaySubjects() {
        adapter.setData(homeViewModel.getSubjects()) // Realiza la solicitud a la API, de materias
        adapter.notifyDataSetChanged()
    }

    private suspend fun setRecyclerView(view: View) {
        binding.RecyclerView.layoutManager = LinearLayoutManager(view.context)

        adapter = SubjectRecyclerViewAdapter { selectedSubject ->
            showSelectedItem(selectedSubject)
        }

        binding.RecyclerView.adapter = adapter

        // Mostrar ProgressBar y ocultar RecyclerView
        binding.progressBar.visibility = View.VISIBLE
        binding.RecyclerView.visibility = View.GONE

        displaySubjects()

        // Ocultar ProgressBar y mostrar RecyclerView
        binding.progressBar.visibility = View.GONE
        binding.RecyclerView.visibility = View.VISIBLE

        binding.RecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
        builder.setTitle("Cerrar aplicación")
        builder.setMessage("¿Estás seguro que quieres cerrar la aplicación?")
        builder.setPositiveButton("Sí") { _, _ ->
            requireActivity().finishAffinity()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
        }
        dialog.show()
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.white)
        window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.alert_dialog_width),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.decorView?.setBackgroundResource(R.drawable.dialog_background)
    }
}