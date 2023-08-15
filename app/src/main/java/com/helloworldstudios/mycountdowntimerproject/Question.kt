package com.helloworldstudios.mycountdowntimerproject

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int
)
