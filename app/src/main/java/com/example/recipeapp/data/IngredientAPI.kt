package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class IngredientAPI(
    val items: ArrayList<IngredientItem>
)

@Serializable
data class IngredientItem(
    val uid: String,
    val title: String,
    val thumbnailUrl: String,
    val descriptionBlocks: ArrayList<DescriptionBlock>,
    val shortDescription: String
)
