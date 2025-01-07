package com.example.recipeapp.http

import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.CollectionAPI
import com.example.recipeapp.data.IngredientAPI
import retrofit2.http.GET

interface RecipeApiService {
    @GET("/recipetags")
    suspend fun getRecipeTags(): ArrayList<String>

    @GET("/categories")
    suspend fun getCategories(): ArrayList<CategoryAPI>

    @GET("/collections")
    suspend fun getCollections(): ArrayList<CollectionAPI>

    @GET("/ingredients")
    suspend fun getIngredients(): ArrayList<IngredientAPI>
}