package com.example.recipeapp.http

import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.CollectionAPI
import com.example.recipeapp.data.IngredientAPI
import com.example.recipeapp.data.RecipeAPI
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("/recipetags")
    suspend fun getRecipeTags(): ArrayList<String>

    @GET("/categories")
    suspend fun getCategories(): CategoryAPI

    @GET("/collections")
    suspend fun getCollections(): CollectionAPI

    @GET("/ingredients")
    suspend fun getIngredients(): IngredientAPI

    @GET("/recipes")
    suspend fun getRecipes(): RecipeAPI
}