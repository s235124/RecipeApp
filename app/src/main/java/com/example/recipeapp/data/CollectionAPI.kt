package com.example.recipeapp.data

data class CollectionAPI(
    val uid: String,
    val title: String,
    val thumbnailUrl: String,
    val description: String,
    val recipeCount: Int,
    val categoryId: String
)