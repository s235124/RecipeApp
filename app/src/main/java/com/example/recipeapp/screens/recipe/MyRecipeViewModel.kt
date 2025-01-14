package com.example.recipeapp.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.MyRecipeDataStore
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.getMyRecipes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MyRecipeViewModel(val context: Context) : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        viewModelScope.launch {
            try {


                getMyRecipes(context).collect { recipeList ->
                    _recipes.value = recipeList
                    println("Recipes in ViewModel: $recipeList")
                }
            }catch (e: Exception){
                println("Error fetching recipes: ${e.message}")
            }
        }
    }

}

