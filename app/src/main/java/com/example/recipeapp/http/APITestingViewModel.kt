package com.example.recipeapp.http

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.CollectionAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// VIEWMODEL FOR TESTING, NEEDS TO BE IMPLEMENTED PROPERLY WHEN MAKING THE ACTUAL VIEWMODELS
class APITestingViewModel : ViewModel() {
    private val _data = MutableStateFlow<CollectionAPI>(CollectionAPI())
    val data: StateFlow<CollectionAPI> = _data

    init {
        fetchData()
    }

    private fun fetchData() {
        // Launch the network call in an IO thread for efficiency
        viewModelScope.launch {
            try {
                // Fetch the data asynchronously
                val fetchedData = RetrofitInstance.api.getCollections()

                // Log the fetched data to verify the response
                Log.d("APITestingViewModel", "Fetched Data: $fetchedData")

                // Update the StateFlow with the fetched data
                _data.value = fetchedData
            } catch (f: Exception) {
                // Handle error appropriately (e.g., logging or user notification)
                f.printStackTrace()
            }
        }
    }

}

