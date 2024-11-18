package com.example.recipeapp.model

data class Category(
    val name: String,
    val flagResId: Int,
    val recipes: List<Recipe>
)
