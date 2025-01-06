package com.example.recipeapp.data

data class Category(
    val name: String,
    val flagResId: Int,
    val recipes: List<Recipe>
)
