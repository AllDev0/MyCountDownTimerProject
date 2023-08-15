package com.helloworldstudios.mycountdowntimerproject

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.snackbar.Snackbar
import com.helloworldstudios.mycountdowntimerproject.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var countDownTimer: CountDownTimer
    private var currentQuestionIndex = 0
    private val questionList = mutableListOf<Question>()
    private val answerList = mutableListOf<RadioButton>()
    private var defaultOptionBackground: Int = R.drawable.default_radio_button_background

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        questionList.add(Question("What is the capital of France?", listOf("London", "Paris"), 1))
        questionList.add(Question("What is the capital of Turkey?", listOf("London", "Ankara"), 1))
        questionList.add(Question("What is the capital of Azerbaijan?", listOf("Baku", "Paris"), 0))

        answerList.add(binding.rbAnswer1)
        answerList.add(binding.rbAnswer2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayQuestion()

        binding.rgAnswers.setOnCheckedChangeListener { _, checkedId ->
            val selectedOption = binding.rgAnswers.findViewById<RadioButton>(checkedId)
            val isCorrect = isCorrectOptionSelected(selectedOption)

            if (isCorrect) {
                selectedOption.setBackgroundResource(R.drawable.correct_option_background)
            } else {
                selectedOption.setBackgroundResource(R.drawable.wrong_option_background)
            }

            countDownTimer.cancel()
            if (isCorrect || currentQuestionIndex == questionList.size - 1) {
                startNewQuestion()
            } else {
                countDownTimer.start()
            }
        }

        countDownTimer = object : CountDownTimer(6000, 1000) {
            override fun onTick(p0: Long) {
                binding.tvTimer.text = "Time: ${p0 / 1000}"
            }

            override fun onFinish() {
                startNewQuestion()
            }
        }.start()
    }

    fun displayQuestion() {
        binding.tvQuestion.text = questionList[currentQuestionIndex].questionText
        binding.rbAnswer1.text = questionList[currentQuestionIndex].options[0]
        binding.rbAnswer2.text = questionList[currentQuestionIndex].options[1]
    }

    private fun isCorrectOptionSelected(selectedOption: RadioButton): Boolean {
        val selectedText = selectedOption.text.toString()
        return selectedText == getCurrentQuestion().options[getCurrentQuestion().correctOptionIndex]
    }

    private fun getCurrentQuestion(): Question {
        return questionList[currentQuestionIndex]
    }

    private fun startNewQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questionList.size) {
            countDownTimer.cancel()
            resetOptionBackgrounds()
            displayQuestion()
            countDownTimer.start()
        } else {
            Snackbar.make(requireView(), "Time is over!\nPlay Again?", Snackbar.LENGTH_INDEFINITE).setAction("Yes") {
                currentQuestionIndex = 0
                countDownTimer.cancel()
                displayQuestion()
                countDownTimer.start()
            }.show()
        }
    }

    private fun resetOptionBackgrounds() {
        for (option in answerList) {
            option.setBackgroundResource(defaultOptionBackground)
        }
    }
}
