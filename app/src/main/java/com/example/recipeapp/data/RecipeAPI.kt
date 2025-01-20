package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeAPI(
    val items: ArrayList<RecipeItem>
) {
    constructor() : this(ArrayList())

}

@Serializable
data class RecipeItem(
    val uid: String? = null,
    val thumbnailUrl: String? = null,
    val collectionsIds: ArrayList<String>? = null,
    val carbs: Int? = null,
    val cookingTime: Int? = null,
    val description: String,
    val difficult: String? = null,
    val fat: Int? = null,
    val fibre: Int? = null,
    val ingredients: ArrayList<RecipeIngredient>? = null,
    val kcal: Int? = null,
    val nutrition: String? = null,
    val preparationTime: Int? = null,
    val protein: Int? = null,
    val salt: Int? = null,
    val saturates: Int? = null,
    val serves: String? = null,
    val steps: ArrayList<String>,
    val sugars: Int? = null,
    val tags: ArrayList<String>? = null,
    val categoriesIds: ArrayList<String>? = null
)

@Serializable
data class RecipeIngredient(
    val uid: String? = null,
    val name: String? = null,
    val description: String
)