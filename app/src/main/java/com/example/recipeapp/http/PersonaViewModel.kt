package com.example.recipeapp.http

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// VIEWMODEL FOR TESTING, NEEDS TO BE IMPLEMENTED PROPERLY WHEN MAKING THE ACTUAL VIEWMODELS
class PersonaViewModel : ViewModel() {
    private val _tags = MutableStateFlow<ArrayList<String>>(ArrayList())
    val tags: StateFlow<ArrayList<String>> = _tags

    init {
        fetchPersonas()
    }

    private fun fetchPersonas() {
        // Launch the network call in an IO thread for efficiency
        viewModelScope.launch {
            try {
                // Fetch the personas asynchronously
                val fetchedRecipeTags = RetrofitInstance.api.getRecipeTags()

                // Log the fetched data to verify the response
                Log.d("PersonaViewModel", "Fetched Personas: $fetchedRecipeTags")

                // Update the StateFlow with the fetched data
                _tags.value = fetchedRecipeTags
            } catch (f: Exception) {
                // Handle error appropriately (e.g., logging or user notification)
                f.printStackTrace()
            }
        }
    }

}

