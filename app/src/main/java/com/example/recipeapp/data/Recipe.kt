package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val name: String,
    val time: String,
    val difficulty: String,
    val description: String,
    val ingredient: List<String>,
    val method: List<String>,
    val calories: String,
    val imageUri: String? = null,
    val imageRes: Int? = null,
    val categories: String,
    val method: String
)

