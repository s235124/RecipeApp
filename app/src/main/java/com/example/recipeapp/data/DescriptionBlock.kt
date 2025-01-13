package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class DescriptionBlock(
    val title: String,
    val content: String
)
