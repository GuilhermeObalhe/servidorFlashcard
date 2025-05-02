package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class BasicFlashcard(
    val id: Long? = null,
    val subjectId: Long,
    val front: String,
    val back: String,
    val lastLocationId: Long?,
    val reviewTime: Long
)
