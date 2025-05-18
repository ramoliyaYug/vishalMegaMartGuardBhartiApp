package com.example.vishalmegamartapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.vishalmegamartapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        fetchUserData()

        binding.btnStartTest.setOnClickListener {
            if (currentUser?.hasAttemptedQuiz == true) {
                showAlreadyCompletedMessage()
            } else {
                startActivity(Intent(this, QuizActivity::class.java))
            }
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun fetchUserData() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.reference.child("vishalmegamartcandidates").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    currentUser = user
                    updateUI(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updateUI(user: User) {
        binding.tvUserInfo.text = "Welcome, ${user.name}"

        if (user.hasAttemptedQuiz) {
            binding.tvTestStatus.visibility = View.VISIBLE
            binding.tvTestStatus.text = getString(R.string.test_score, user.quizScore, user.totalQuestions)
            binding.btnStartTest.text = "View Results"
        } else {
            binding.tvTestStatus.visibility = View.GONE
            binding.btnStartTest.text = getString(R.string.start_test)
        }
    }

    private fun showAlreadyCompletedMessage() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE", currentUser?.quizScore ?: 0)
        intent.putExtra("TOTAL_QUESTIONS", currentUser?.totalQuestions ?: 0)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        fetchUserData()
    }
}
