package com.example.sit305_31pquizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var options: List<Button>
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var questionCount: TextView

    private var currentQuestionIndex = 0
    private var selectedAnswer = -1
    private var score = 0
    private var isAnswerSubmitted = false

    private val questions = listOf(
        Question("Android is developed by?", listOf("Google", "Apple", "Microsoft"), 0),
        Question("Latest Android version?", listOf("12", "13", "14"), 2),
        Question("Jetpack is?", listOf("Library", "Framework", "Toolkit"), 2),
        Question("Kotlin is?", listOf("Language", "OS", "IDE"), 0),
        Question("Android Studio is based on?", listOf("Eclipse", "Netbeans", "IntelliJ"), 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.textQuestion)
        options = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3)
        )
        submitButton = findViewById(R.id.buttonSubmit)
        progressBar = findViewById(R.id.progressBar)
        questionCount = findViewById(R.id.textQuestionCount)

        loadQuestion()

        options.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (!isAnswerSubmitted) {
                    selectedAnswer = index
                    highlightSelection(index)
                }
            }
        }

        submitButton.setOnClickListener {
            if (!isAnswerSubmitted) {
                if (selectedAnswer == -1) {
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                } else {
                    evaluateAnswer()
                    isAnswerSubmitted = true
                    submitButton.text = if (currentQuestionIndex == questions.lastIndex) {
                        getString(R.string.finish)
                    } else {
                        getString(R.string.next)
                    }
                }
            } else {
                if (currentQuestionIndex == questions.lastIndex) {
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("total", questions.size)
                    startActivity(intent)
                    finish()
                } else {
                    currentQuestionIndex++
                    selectedAnswer = -1
                    isAnswerSubmitted = false
                    loadQuestion()
                    submitButton.text = getString(R.string.submit)
                }
            }
        }
    }

    private fun loadQuestion() {
        val q = questions[currentQuestionIndex]
        questionText.text = q.text
        questionCount.text = getString(R.string.question_progress, currentQuestionIndex + 1, questions.size)
        progressBar.progress = ((currentQuestionIndex + 1) * 100) / questions.size

        q.options.forEachIndexed { i, option ->
            options[i].text = option
            options[i].isEnabled = true
            options[i].setBackgroundColor(getColor(R.color.default_option))
        }
    }

    private fun highlightSelection(index: Int) {
        options.forEachIndexed { i, button ->
            button.setBackgroundColor(
                if (i == index) getColor(R.color.selected_option)
                else getColor(R.color.default_option)
            )
        }
    }

    private fun evaluateAnswer() {
        val correctIndex = questions[currentQuestionIndex].correctAnswerIndex

        options[correctIndex].setBackgroundColor(getColor(R.color.correct_option))

        if (selectedAnswer != correctIndex) {
            options[selectedAnswer].setBackgroundColor(getColor(R.color.incorrect_option))
        } else {
            score++
        }

        options.forEach { it.isEnabled = false }
    }
}
