package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(val id: Int? = null, val name: String)
