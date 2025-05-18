package com.example.vishalmegamartapp

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val quizScore: Int = 0,
    val totalQuestions: Int = 0,
    val hasAttemptedQuiz: Boolean = false,
    val isAdmin: Boolean = false
)
