package com.example.recipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
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
import com.example.recipeapp.model.Chips
import com.example.recipeapp.model.CustomSearchBar
import com.example.recipeapp.model.Grids
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, recipes: List<Recipe>, categories: List<Category>) {
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(color = Color.LightGray),
        topBar = { // Change to top bar comp
            CenterAlignedTopAppBar(
                title = { Text("Welcome") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Chips(
                    categories = categories,
                    onCategoryClick = { name ->
                        navController.navigate("${Route.CategoryRecipesScreen.title}/${name}")
                    },
                    onViewAllClick = {
                        // Handle "View All Categories" click, navigate to another screen if needed
                        navController.navigate(Route.AllCategoriesScreen.title)
                    }
                )

                // Pass categories to the Chips function
                Grids(
                    recipes = recipes,
                    navController = navController
                ) // Pass recipes to the Grids function
            }
        },
    )
}