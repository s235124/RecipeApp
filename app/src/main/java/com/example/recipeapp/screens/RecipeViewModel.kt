package com.example.recipeapp.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.http.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _data = MutableStateFlow(RecipeAPI())
    val data: StateFlow<RecipeAPI> = _data

    init {
        fetchData()
    }

    private fun fetchData() {
        // Launch the network call
        viewModelScope.launch {
            try {
                // Fetch the data asynchronously
                val fetchedData = RetrofitInstance.api.getRecipes()

                // Log the fetched data to verify the response
                Log.d("RecipeViewModel", "Fetched Recipes: ${fetchedData.items.size}")

                // Update the StateFlow with the fetched data
                _data.value = fetchedData
            } catch (f: Exception) {
                // Handle error appropriately (e.g., logging or user notification)
                f.printStackTrace()
            }
        }
    }
}