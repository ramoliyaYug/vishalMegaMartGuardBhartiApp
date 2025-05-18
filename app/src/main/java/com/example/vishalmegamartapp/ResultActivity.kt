package com.example.vishalmegamartapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.vishalmegamartapp.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val score = intent.getIntExtra("SCORE", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)

        binding.tvScore.text = getString(R.string.score, score, totalQuestions)

        val percentage = (score.toFloat() / totalQuestions) * 100
        binding.tvPercentage.text = "Percentage: ${String.format("%.1f", percentage)}%"

        binding.tvFeedback.text = when {
            percentage >= 80 -> "Bhai wah! Tum to guard ke naam pe full James Bond nikle! CCTV bhi sharma jaye."
            percentage >= 60 -> "Theek hai yaar, thoda aur polish karo, walkie-talkie king ban jaoge!"
            percentage >= 40 -> "Chalo chalo, CCTV tumse zyada alert hai, par tum bhi kuch kam nahi!"
            else -> "Arre Romeo, guard banna hai ya Vishal Mega Mart ka missing item? Dobara try maaro!"
        }

        saveResultToFirebase(score, totalQuestions)

        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun saveResultToFirebase(score: Int, totalQuestions: Int) {
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("vishalmegamartcandidates").child(userId)
            .child("quizScore").setValue(score)

        database.reference.child("vishalmegamartcandidates").child(userId)
            .child("totalQuestions").setValue(totalQuestions)

        database.reference.child("vishalmegamartcandidates").child(userId)
            .child("hasAttemptedQuiz").setValue(true)
    }
}
