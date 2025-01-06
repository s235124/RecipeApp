package com.example.recipeapp.data

data class CategoryAPI(
    val uid: String,
    val title: String,
    val thumbnailURL: String,
    val description: String,
    val collectionsCount: Int
)
