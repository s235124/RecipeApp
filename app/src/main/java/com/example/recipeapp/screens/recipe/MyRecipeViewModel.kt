package com.example.recipeapp.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.getMyRecipes
import com.example.recipeapp.data.saveMyRecipes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val updatedList = _recipes.value.toMutableList()
                updatedList.remove(recipe)
                _recipes.value = updatedList

                // Optionally update persistent storage if necessary
                saveMyRecipes(context,updatedList)
            } catch (e: Exception) {
                println("Error deleting recipe: ${e.message}")
            }
        }
    }

    fun deleteAllRecipes() {
        viewModelScope.launch {
            try {
                val updatedList = emptyList<Recipe>()
                _recipes.value = updatedList

                // Optionally update persistent storage if necessary
                saveMyRecipes(context,updatedList)
            } catch (e: Exception) {
                println("Error deleting recipe: ${e.message}")
            }
        }
    }
}



