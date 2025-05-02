package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizFlashcard(
    val id: Long? = null,
    val subjectId: Long,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val lastLocationId: Long?,
    val reviewTime: Long
)
