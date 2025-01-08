package com.example.recipeapp.data

import kotlinx.serialization.Serializable

@Serializable
data class CollectionAPI(
    val items: ArrayList<CollectionItem>
) {
    constructor():this(ArrayList<CollectionItem>())
}

@Serializable
data class CollectionItem(
    val uid: String,
    val title: String,
    val thumbnailUrl: String,
    val description: String,
    val recipesCount: Int,
    val categoryId: String
)