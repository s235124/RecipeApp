package com.example.recipeapp.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.http.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _categories = MutableStateFlow(CategoryAPI())
    val categories: StateFlow<CategoryAPI> = _categories

    private var currentPage = 0

    init {
        if (currentPage < 1) {
            viewModelScope.launch {
                try {
                    val categoryPage = RetrofitInstance.api.getCategory(1)

                    Log.d("New category", "$categoryPage")

                    val updatedItems = ArrayList(_categories.value.items)
                    updatedItems.addAll(categoryPage.items)

                    _categories.update { currentCategories ->
                        currentCategories.copy(items = updatedItems)
                    }

                    Log.d(
                        "CategoriesViewModel",
                        "Total categories: ${_categories.value.items.size}"
                    )

                    currentPage = 1  // Update current page after successful fetch
                } catch (e: Exception) {
                    Log.e("CategoriesViewModel", "Error fetching categories", e)
                }
            }
        }
    }

    fun getCategory(pageNum: Int) {
        if (pageNum > currentPage) {
            viewModelScope.launch {
                try {
                    val categoryPage = RetrofitInstance.api.getCategory(pageNum)

                    Log.d("New category", "$categoryPage")

                    val updatedItems = ArrayList(_categories.value.items)
                    updatedItems.addAll(categoryPage.items)

                    _categories.update { currentCategories ->
                        currentCategories.copy(items = updatedItems)
                    }

                    Log.d(
                        "CategoriesViewModel",
                        "Total categories: ${_categories.value.items.size}"
                    )

                    currentPage = pageNum  // Update current page after successful fetch
                } catch (e: Exception) {
                    Log.e("CategoriesViewModel", "Error fetching categories", e)
                }
            }
        }
    }



//    fun getCategory(pageNum: Int) {
//        viewModelScope.launch {
//            try {
//                val categoryPage = RetrofitInstance.api.getCategory(pageNum)
//                val currentItems = _categories.value.items
//                if (categoryPage.items.isNotEmpty() && categoryPage.items != currentItems) {
//                    _categories.value = CategoryAPI(items = ArrayList(currentItems + categoryPage.items))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}