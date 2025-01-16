package com.example.recipeapp.screens.createmyscreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.getImageUri
import com.example.recipeapp.data.getMyRecipes
import com.example.recipeapp.data.saveImageUriToDataStore
import com.example.recipeapp.data.saveMyRecipes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class CreateMyRecipeViewModel(val context: Context) : ViewModel() {



    // StateFlow to observe the list of recipes
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> get() = _recipes


    private val _imageUriFlow = MutableStateFlow<String?>(null)
    val imageUriFlow = _imageUriFlow.asStateFlow() // Expose as read-only




    init {
        viewModelScope.launch {
            try {
                _recipes.value = getMyRecipes(context).first()
                val savedUri = getImageUri(context).firstOrNull()
                _imageUriFlow.value = savedUri
            } catch (e: Exception) {
                // Handle error gracefully (e.g., log error, show fallback UI)
                _recipes.value = emptyList()
                e.printStackTrace()
            }
        }
    }


    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                // Add the new recipe to the existing list
                val updatedRecipes = _recipes.value.toMutableList().apply { add(recipe) }
                // Save the updated list to DataStore
                saveMyRecipes(context, updatedRecipes)
                // Update the in-memory state only after saving
                _recipes.value = updatedRecipes
                Log.d("com.example.recipeapp.screens.createmyscreen.CreateMyRecipeViewModel", "Recipe saved: $updatedRecipes")
            } catch (e: Exception) {
                // Log or handle errors (optional)
                Log.e("com.example.recipeapp.screens.createmyscreen.CreateMyRecipeViewModel", "Error saving recipe: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun saveImageUri(uri: String) {
        viewModelScope.launch {
            saveImageUriToDataStore(context, uri)// Save to DataStore
            _imageUriFlow.value = uri // Update StateFlow
        }
    }

}
