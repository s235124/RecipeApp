package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeAPI(
    val items: ArrayList<RecipeItem>
)

@Serializable
data class RecipeItem(
    val uid: String,
    val thumbnailURL: String,
    val collectionIds: ArrayList<String>,
    val carbs: Int,
    val cookingTime: Int,
    val description: String,
    val difficult: String,
    val fat: Int,
    val fibre: Int,
    val ingredients: ArrayList<RecipeIngredient>,
    val kcal: Int,
    val nutrition: String,
    val preparationTime: Int,
    val protein: Int,
    val salt: Int,
    val saturates: Int,
    val serves: String,
    val steps: ArrayList<String>,
    val sugars: Int,
    val tags: ArrayList<String>,
    val categoriesIds: ArrayList<String>
)

@Serializable
data class RecipeIngredient(
    val uid: String,
    val name: String,
    val description: String
)