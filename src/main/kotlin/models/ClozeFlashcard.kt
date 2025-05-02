package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ClozeFlashcard(
    val id: Long? = null,
    val subjectId: Long,
    val fullText: String,
    val gaps: List<String>,
    val lastLocationId: Long?,
    val reviewTime: Long
)