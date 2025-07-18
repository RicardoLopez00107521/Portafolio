package com.avanzo.avanzotestsimulator.ui.tests

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.ActivityTestBinding
import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.network.dto.quiz.ScoreDTO
import com.avanzo.avanzotestsimulator.ui.home.TestDetailsActivity
import com.avanzo.avanzotestsimulator.ui.home.TestDetailsActivity.Companion.SUBJECT_ID
import com.avanzo.avanzotestsimulator.ui.tests.viewmodel.TestViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class TestActivity : AppCompatActivity(), View.OnClickListener {
    private val testViewModel: TestViewModel by viewModels {
        TestViewModel.Factory
    }

    private var questionsIds: MutableList<Int> = mutableListOf()
    private var answer: MutableList<String> = mutableListOf()
    private var answerWithQuestions: MutableList<Pair<Int, String>> = mutableListOf()

    private var subjectId: Int = 1

    private lateinit var binding: ActivityTestBinding
    private var timer: CountDownTimer? = null

    private var currentPosition: Int = 1
    private var isOptionSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = (application as AvanzoReviewerApplication)
        val token = app.getToken()

        if (intent.hasExtra("subjectId")) {
            subjectId = intent.getLongExtra("subjectId", 0L).toInt()
            Log.d("TestActivity", "Obteniendo: $subjectId")
        }

        binding.backButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        // Setting up the question and timer
        CoroutineScope(Dispatchers.Main).launch {
            try {
                binding.progressBar2.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE

                setQuestion() // Function that fetches and displays the questions

                binding.progressBar2.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.firstButton.setOnClickListener(this)
        binding.secondButton.setOnClickListener(this)
        binding.thirdButton.setOnClickListener(this)
        binding.fourthButton.setOnClickListener(this)
        binding.nextQuestion.setOnClickListener(this)

        timer = object : CountDownTimer(45 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                val timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                binding.timerTextView.text = timeLeft
            }

            override fun onFinish() {
                Log.d("TestActivity", "questionsIds: $questionsIds")
                Log.d("TestActivity", "answer: $answer")

                val bundle = Bundle().apply {
                    putSerializable("answerWithQuestions", ArrayList(answerWithQuestions))

                }

                val intent = Intent(this@TestActivity, ResultTestActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }

        timer?.start()
    }

    private suspend fun postAnswersBeforeSettingQuestions() :ApiResponse<Float> {
        return testViewModel.postAnswers("Test", subjectId, questionsIds, answer)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private suspend fun setQuestion() {
        val question = testViewModel.getQuestionBySubject(subjectId)[currentPosition - 1]
        val questions = testViewModel.getQuestionBySubject(subjectId)

        defaultOptionView()

        if (currentPosition == 1) {
            binding.previousQuestion.visibility = View.GONE
        } else {
            binding.previousQuestion.visibility = View.VISIBLE
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

        binding.nextQuestion.setBackgroundResource(R.drawable.disabled_background)
    }

    private fun defaultOptionView() {
        val options = ArrayList<Button>()
        options.add(0, binding.firstButton)
        options.add(1, binding.secondButton)
        options.add(2, binding.thirdButton)
        options.add(3, binding.fourthButton)

        for (option in options) {
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.button_pressed)
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
                R.id.next_question -> {
                    if (isOptionSelected) {
                        currentPosition++
                        if (currentPosition <= testViewModel.getQuestionBySubject(subjectId).size) {
                            setQuestion()
                            isOptionSelected = false
                        } else {
                            // Llamar a postAnswersBeforeSettingQuestions() y manejar la respuesta
                            when (val result = postAnswersBeforeSettingQuestions()) {
                                is ApiResponse.Success -> {
                                    val correctAnswers = result.data.toInt()

                                    val bundle = Bundle().apply {
                                        putSerializable("answerWithQuestions", ArrayList(answerWithQuestions))
                                    }
                                    bundle.putSerializable("correctAns", correctAnswers)
                                    val intent = Intent(this@TestActivity, ResultTestActivity::class.java)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    finish()
                                }
                                is ApiResponse.ErrorWithMessage -> {
                                    Toast.makeText(this@TestActivity, result.message, Toast.LENGTH_SHORT).show()
                                }
                                is ApiResponse.Error -> {
                                    Toast.makeText(this@TestActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private suspend fun selectOption(selectedOptionNum: Int) {
        if (!isOptionSelected) {
            val question = testViewModel.getQuestionBySubject(subjectId)[currentPosition - 1]
            val originalId = question.numP
            questionsIds.add(originalId)

            val selectedOptionText = when (selectedOptionNum) {
                1 -> question.optionOne
                2 -> question.optionTwo
                3 -> question.optionThree
                4 -> question.optionFour
                else -> ""
            }

            answer.add(selectedOptionText)
            answerWithQuestions.add(Pair(originalId, selectedOptionText))

            answerView(selectedOptionNum, R.drawable.enabled_background)

            isOptionSelected = true
            binding.nextQuestion.setBackgroundResource(R.drawable.enabled_background)
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.firstButton.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                binding.secondButton.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                binding.thirdButton.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                binding.fourthButton.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Cerrar prueba")
        builder.setMessage("¿Estás seguro que deseas salir de la prueba? No se guardará el progreso")
        builder.setPositiveButton("Sí") { _, _ ->
            val intent = Intent(this, TestDetailsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
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
        window?.setLayout(resources.getDimensionPixelSize(R.dimen.alert_dialog_width), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.decorView?.setBackgroundResource(R.drawable.dialog_background)
    }
}
