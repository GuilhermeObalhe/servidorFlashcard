package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val id: Long? = null,
    val name: String
)