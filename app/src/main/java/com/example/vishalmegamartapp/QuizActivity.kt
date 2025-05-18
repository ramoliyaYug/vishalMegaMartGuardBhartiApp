package com.example.vishalmegamartapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.RadioButton
import android.widget.Toast
import com.example.vishalmegamartapp.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val questions = QuizRepository.getQuestions()
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        checkQuizAttemptStatus()

        displayQuestion()

        binding.btnNext.setOnClickListener {
            val selectedOption = binding.radioGroup.checkedRadioButtonId

            if (selectedOption == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radioButton = findViewById<RadioButton>(selectedOption)
            val selectedOptionIndex = binding.radioGroup.indexOfChild(radioButton)

            if (selectedOptionIndex == questions[currentQuestionIndex].correctAnswer) {
                score++
            }

            currentQuestionIndex++

            if (currentQuestionIndex < questions.size) {
                displayQuestion()
            } else {
                saveQuizResults()
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("SCORE", score)
                intent.putExtra("TOTAL_QUESTIONS", questions.size)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun checkQuizAttemptStatus() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.reference.child("vishalmegamartcandidates").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null && user.hasAttemptedQuiz) {
                    Toast.makeText(this@QuizActivity, "You have already completed the quiz", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                    intent.putExtra("SCORE", user.quizScore)
                    intent.putExtra("TOTAL_QUESTIONS", user.totalQuestions)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun displayQuestion() {
        binding.radioGroup.clearCheck()

        val currentQuestion = questions[currentQuestionIndex]
        binding.tvQuestion.text = currentQuestion.questionText
        binding.rbOption1.text = currentQuestion.options[0]
        binding.rbOption2.text = currentQuestion.options[1]
        binding.rbOption3.text = currentQuestion.options[2]
        binding.rbOption4.text = currentQuestion.options[3]

        binding.tvQuestionNumber.text = "Question ${currentQuestionIndex + 1}/${questions.size}"

        if (currentQuestionIndex == questions.size - 1) {
            binding.btnNext.text = getString(R.string.finish)
        }
    }

    private fun saveQuizResults() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.reference.child("vishalmegamartcandidates").child(userId)

        userRef.child("quizScore").setValue(score)
        userRef.child("totalQuestions").setValue(questions.size)
        userRef.child("hasAttemptedQuiz").setValue(true)
    }
}
