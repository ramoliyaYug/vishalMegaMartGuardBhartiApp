package com.example.vishalmegamartapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vishalmegamartapp.databinding.ActivityAdminRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val ADMIN_KEY = "vishal123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val adminKey = binding.etAdminKey.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || adminKey.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (adminKey != ADMIN_KEY) {
                Toast.makeText(this, "Invalid admin key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerAdmin(name, email, password)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun registerAdmin(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val user = User(
                        userId = userId,
                        name = name,
                        email = email,
                        isAdmin = true
                    )

                    database.reference.child("vishalmegamartcandidates").child(userId).setValue(user)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                database.reference.child("vishalmegamartcandidates").child(userId)
                                    .child("isAdmin").setValue(true)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Admin registration successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, AdminDashboardActivity::class.java))
                                        finish()
                                    }
                            } else {
                                Toast.makeText(this, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
