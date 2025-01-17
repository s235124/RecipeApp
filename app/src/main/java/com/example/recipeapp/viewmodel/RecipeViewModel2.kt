package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.recipeapp.data.Recipe

class RecipeViewModel2 : ViewModel() {
    private val _chosenRecipe = mutableStateOf<Recipe?>(null)
    val chosenRecipe: State<Recipe?> get() = _chosenRecipe


    fun setRecipe(recipe: Recipe) {
        _chosenRecipe.value = recipe
    }
}