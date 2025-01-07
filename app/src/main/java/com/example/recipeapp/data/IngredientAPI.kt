package com.example.recipeapp.data

data class IngredientAPI(
    val uid: String,
    val title: String,
    val thumbnailUrl: String,
    val descriptionBlocks: ArrayList<DescriptionBlock>,
    val shortDescription: String
)
