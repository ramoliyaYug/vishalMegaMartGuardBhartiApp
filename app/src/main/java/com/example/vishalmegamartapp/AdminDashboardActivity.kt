package com.example.vishalmegamartapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vishalmegamartapp.databinding.ActivityAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        loadStatistics()

        binding.btnViewLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadStatistics() {
        val usersRef = database.reference.child("vishalmegamartcandidates")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalUsers = -1
                var totalScore = 0
                var totalAttempts = 0
                var highestScore = 0
                var totalQuestions = 0

                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        totalUsers++

                        if (user.hasAttemptedQuiz) {
                            totalScore += user.quizScore
                            totalAttempts++

                            if (user.quizScore > highestScore) {
                                highestScore = user.quizScore
                                totalQuestions = user.totalQuestions
                            }
                        }
                    }
                }

                binding.tvTotalUsers.text = getString(R.string.total_users, totalUsers)

                val averageScore = if (totalAttempts > 0) {
                    (totalScore.toFloat() / totalAttempts) * 10
                } else {
                    0f
                }

                binding.tvAverageScore.text = getString(R.string.average_score, averageScore)
                binding.tvHighestScore.text = getString(R.string.highest_score, highestScore, totalQuestions)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
