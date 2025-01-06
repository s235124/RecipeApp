package com.example.recipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.recipeapp.BottomBar
import com.example.recipeapp.Chips
import com.example.recipeapp.CustomSearchBar
import com.example.recipeapp.Grids
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, recipes: List<Recipe>, categories: List<Category>) {
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(color = Color.LightGray),
        topBar = { // Change to top bar comp
            TopAppBar(
                title = { Text("Welcome") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                CustomSearchBar(
                    searchQuery.value,
                    onSearchQueryChange = { query ->
                        searchQuery.value = query
                    },
                    navController = navController
                )

                Chips(
                    categories = categories,
                    onViewAllClick = {
                        // Handle "View All Categories" click, navigate to another screen if needed
                        navController.navigate("allCategories")
                    },
                    navController = navController
                )

                // Pass categories to the Chips function
                Grids(
                    recipes,
                    navController = navController
                ) // Pass recipes to the Grids function
            }
        },
    )
}