package com.avanzo.avanzotestsimulator.ui.tests

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.ActivityTrainingTestBinding
import com.avanzo.avanzotestsimulator.ui.home.TestDetailsActivity
import com.avanzo.avanzotestsimulator.ui.tests.viewmodel.TestViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainingTestActivity : AppCompatActivity(), View.OnClickListener {

    private val testViewModel: TestViewModel by viewModels {
        TestViewModel.Factory
    }
    private var answerWithQuestions: MutableList<MutableList<Long>>  = mutableListOf()// [[1,1], [1,0]]

    private var subjectId: Int = 1

    private var isOptionSelected: Boolean = false

    private lateinit var binding: ActivityTrainingTestBinding

    private var currentPosition: Int = 1
    private var correctAnswers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subjectId = intent.getLongExtra("subjectId", 0L).toInt()

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.backButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        CoroutineScope(Dispatchers.Main).launch {

            binding.progressBar2.visibility = View.VISIBLE
            binding.scrollView.visibility = View.GONE

            setQuestion() // Función que trae las preguntas

            binding.progressBar2.visibility = View.GONE
            binding.scrollView.visibility = View.VISIBLE

        }

        binding.firstButton.setOnClickListener(this)
        binding.secondButton.setOnClickListener(this)
        binding.thirdButton.setOnClickListener(this)
        binding.fourthButton.setOnClickListener(this)
        binding.nextQuestion.setOnClickListener(this)
        binding.previousQuestion.setOnClickListener(this)

    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private suspend fun setQuestion() {
        val question = testViewModel.getQuestionBySubject(subjectId)[currentPosition - 1]
        val questions = testViewModel.getQuestionBySubject(subjectId)

        isOptionSelected = false

        defaultOptionView()

        if (currentPosition == 1) {
            binding.previousQuestion.visibility = View.GONE
        } else {
            binding.previousQuestion.visibility = View.VISIBLE
        }

        if (currentPosition == testViewModel.getQuestionBySubject(subjectId).size) {
            binding.nextQuestion.visibility = View.GONE
        } else {
            binding.nextQuestion.visibility = View.VISIBLE
        }

        val bar = binding.progressBar
        bar.max = questions.size
        binding.progressBar.progress = currentPosition
        binding.tvProgress.text = "$currentPosition/${bar.max}"

        binding.tvQuestion.text = question.question
        binding.firstOptionTv.text = question.optionOne
        binding.secondOptionTv.text = question.optionTwo
        binding.thirdOptionTv.text = question.optionThree
        binding.fourthOptionTv.text = question.optionFour

    }

    private fun defaultOptionView() {

        val options = ArrayList<Button>()
        options.add(0, binding.firstButton)
        options.add(1, binding.secondButton)
        options.add(2, binding.thirdButton)
        options.add(3, binding.fourthButton)


        for (option in options) {
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.button_pressed
            )
        }
    }

    override fun onClick(v: View?) {
        CoroutineScope(Dispatchers.Main).launch {
            when (v?.id) {
                R.id.first_button -> {
                    selectOption(1)
                }
                R.id.second_button -> {
                    selectOption(2)
                }
                R.id.third_button -> {
                    selectOption(3)
                }
                R.id.fourth_button -> {
                    selectOption(4)
                }
                R.id.previous_question -> {
                    currentPosition--
                    setQuestion()
                }
                R.id.next_question -> {
                    currentPosition++
                    if (currentPosition <= testViewModel.getQuestionBySubject(subjectId).size) {
                        setQuestion()
                    }
                }

            }
        }
    }

    private suspend fun selectOption(selectedOptionNum: Int) {

        if (isOptionSelected != true) {
            val question = testViewModel.getQuestionBySubject(subjectId)[currentPosition - 1]

            if (question.correctAnswer != selectedOptionNum) {
                answerView(selectedOptionNum, R.drawable.incorrect_option_bg)
                val newAnswer = mutableListOf(question.numP.toLong(), 0.toLong())
                answerWithQuestions.add(newAnswer)
            } else {
                val newAnswer = mutableListOf(question.numP.toLong(), 1L)
                answerWithQuestions.add(newAnswer)
                correctAnswers++
                answerView(selectedOptionNum, R.drawable.correct_option_bg)
            }

            isOptionSelected = true
            answerView(question.correctAnswer, R.drawable.correct_option_bg)

        }

    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.firstButton.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2 -> {
                binding.secondButton.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3 -> {
                binding.thirdButton.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4 -> {
                binding.fourthButton.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Cerrar práctica")
        builder.setMessage("¿Estás seguro que deseas salir de la práctica?")
        builder.setPositiveButton("Sí") { _, _ ->
            val intent = Intent(this, TestDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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