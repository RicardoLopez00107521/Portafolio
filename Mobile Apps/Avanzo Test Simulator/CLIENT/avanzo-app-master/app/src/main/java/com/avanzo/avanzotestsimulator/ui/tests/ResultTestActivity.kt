package com.avanzo.avanzotestsimulator.ui.tests

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.avanzo.avanzotestsimulator.MainActivity
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.ActivityResultTestBinding
import com.avanzo.avanzotestsimulator.ui.home.ProfileActivity
import java.math.RoundingMode

class ResultTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            showExitConfirmationDialog()
        }
        val cAns = intent.getIntExtra("correctAns", 0)

        try {
            val bundle = intent.extras
            if (bundle != null && bundle.containsKey("answerWithQuestions")) {
                val answerWithQuestionsExtra = bundle.getSerializable("answerWithQuestions") as ArrayList<ArrayList<Long>>

                // Convertir el ArrayList en MutableList<MutableList<Long>>
                val answerWithQuestions = answerWithQuestionsExtra.map { it.toMutableList() }.toMutableList()
                Log.d("TAG", "here ON RESULT!: ${answerWithQuestions.toString()}")
            }
        } catch (e: Exception) {
            //Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }

        val points = cAns * 10
        val messagePoints = "+ $points puntos"
        binding.points.text = messagePoints
        binding.resultQ.text = "$cAns/15"
        binding.result.text = calculateGrade(cAns).toString()

        binding.finishButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.shareButton.setOnClickListener {
            val result = binding.result.text
            val message = "¡Este fue mi puntaje en AVANZO Test Simulator: $result!"
            val intent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }

    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    fun calculateGrade(correctAnswers: Int): Double {
        val grade = (correctAnswers.toDouble() / 15) * 10.0
        return grade.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Salir")
        builder.setMessage("¿Deseas salir al menú principal?")
        builder.setPositiveButton("Sí") { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.main))
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.main))
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