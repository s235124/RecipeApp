package com.example.recipeapp.http

import com.example.recipeapp.data.CategoryAPI
import retrofit2.http.GET

interface RecipeApiService {
    @GET("/recipetags")
    suspend fun getRecipeTags(): ArrayList<String>

    @GET("/categories")
    suspend fun getCategories(): ArrayList<CategoryAPI>
}