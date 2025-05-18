package com.example.vishalmegamartapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vishalmegamartapp.databinding.ActivityAdminLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginAdmin(email, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, AdminRegisterActivity::class.java))
        }

        binding.tvUserLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loginAdmin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkAdminRole(auth.currentUser?.uid ?: "")
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkAdminRole(userId: String) {
        val userRef = database.reference.child("vishalmegamartcandidates").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if (user.isAdmin) {
                        startActivity(Intent(this@AdminLoginActivity, AdminDashboardActivity::class.java))
                        finish()
                    } else {
                        database.reference.child("vishalmegamartcandidates").child(userId)
                            .child("isAdmin").setValue(true)
                            .addOnSuccessListener {
                                Toast.makeText(this@AdminLoginActivity, "Admin access granted", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AdminLoginActivity, AdminDashboardActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@AdminLoginActivity, "Failed to update admin status", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                    }
                } else {
                    Toast.makeText(this@AdminLoginActivity, "User data not found", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminLoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
