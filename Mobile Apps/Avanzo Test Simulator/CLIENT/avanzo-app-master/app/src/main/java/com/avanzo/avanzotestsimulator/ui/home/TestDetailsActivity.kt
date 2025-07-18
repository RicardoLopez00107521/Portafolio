package com.avanzo.avanzotestsimulator.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import com.avanzo.avanzotestsimulator.MainActivity
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.ActivityTestDetailsBinding
import com.avanzo.avanzotestsimulator.ui.home.recyclerview.SubjectRecyclerViewAdapter
import com.avanzo.avanzotestsimulator.ui.home.viewmodel.HomeViewModel
import com.avanzo.avanzotestsimulator.ui.tests.TestActivity
import com.avanzo.avanzotestsimulator.ui.tests.TrainingTestActivity
import com.avanzo.avanzotestsimulator.ui.tests.viewmodel.TestViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestDetailsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTestDetailsBinding

    private val testViewModel: TestViewModel by viewModels {
        TestViewModel.Factory
    }

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory
    }

    private var subjectId: Long = 0L
    private var subjectName: String = ""

    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val SUBJECT_ID = "subjectId"
        const val SUBJECT_NAME = "subjectName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d("HomeFragment", "Queri obtener las preguntas de: ${subject.name}")
                //setRecyclerView(view)
                testViewModel.getQuestions(subject.name)
            } catch (e: Exception) {
                Log.d("HomeFragment", "Mori ${subject.name}")
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }*/

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        subjectId = sharedPreferences.getLong(SUBJECT_ID, 0L)
        subjectName = sharedPreferences.getString(SUBJECT_NAME, "") ?: ""

        Log.d("TestDetailsActivity2", "Antes")

        if (intent.hasExtra("subjectCode")) {
            val code = intent.getStringExtra("subjectCode") ?: "hola"
            subjectId = code.toLong()
            Log.d("TestDetailsActivity2", "Obteniendo: $subjectId")

            // Guardamos el valor en el companion
            val editor = sharedPreferences.edit()
            editor.putLong(SUBJECT_ID, subjectId)
            editor.apply()
        }

        if (intent.hasExtra("subjectNAME")) {
            val subjectName = intent.getStringExtra("subjectNAME") ?: ""
            Log.d("TestDetailsActivity2", "Obteniendo: $subjectName")

            /*CoroutineScope(Dispatchers.Main).launch {
                try {
                    Log.d("HomeFragment", "Queri obtener las preguntas de: ${subjectName}")
                    // setRecyclerView(view)
                    testViewModel.getQuestions(subjectName)
                } catch (e: Exception) {
                    Log.d("HomeFragment", "Mori ${subjectName}")
                    Toast.makeText(this@TestDetailsActivity, "Error", Toast.LENGTH_SHORT).show() // Cambié 'requireContext()' por 'this@TestDetailsActivity'
                }
            }*/

            // Guardamos el valor en el companion
            val editor = sharedPreferences.edit()
            editor.putString(SUBJECT_NAME, subjectName)
            editor.apply()
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }



        binding.TestButton.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            Log.d("TestDetailsActivity2", "Enviando: $subjectId")
            intent.putExtra("subjectId", subjectId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}