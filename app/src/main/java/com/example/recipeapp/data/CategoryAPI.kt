package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAPI(
    val items: ArrayList<CategoryItem>
)

@Serializable
data class CategoryItem(
    val uid: String,
    val title: String,
    val thumbnailURL: String,
    val description: String,
    val collectionsCount: Int
)
