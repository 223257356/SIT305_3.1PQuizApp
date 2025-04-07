package com.example.sit305_31pquizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var btnRestart: Button
    private lateinit var btnFinish: Button
    private val sharedPref by lazy { getSharedPreferences("QuizAppPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        scoreText = findViewById(R.id.textScore)
        btnRestart = findViewById(R.id.btnRestart)
        btnFinish = findViewById(R.id.btnFinish)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 5)
        val name = sharedPref.getString("username", "User")

        val message = getString(R.string.score_message, name, score, total)
        scoreText.text = message

        btnRestart.setOnClickListener {
            // Back to MainActivity (which keeps the saved name)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnFinish.setOnClickListener {
            finishAffinity() // Close the app
        }
    }
}
