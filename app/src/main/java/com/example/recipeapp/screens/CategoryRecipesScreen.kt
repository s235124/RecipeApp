package com.example.recipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.model.RecipeCard
import com.example.recipeapp.data.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRecipesScreen(
    onBackButtonClick: () -> Unit,
    navController: NavController,
    categoryName: String,
    categories: List<Category>
) {
    var searchQuery by remember { mutableStateOf("") }

    // Find the selected category and its recipes
    val category = categories.find { it.name.equals(categoryName, ignoreCase = true) }
    val categoryRecipes = category?.recipes ?: emptyList()

    // Filter recipes based on the search query
    val filteredRecipes = categoryRecipes.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$categoryName Recipes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Enhanced Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> searchQuery = query },
                placeholder = { Text("Search ${categoryName} recipes...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF0F0F0)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF78B17E)
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // Display filtered recipes
            if (filteredRecipes.isEmpty()) {
                Text(
                    text = "No recipes found",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    items(filteredRecipes) { recipe ->
                        RecipeCard(
                            recipe = TODO(),
                            onNavigate = TODO(),
                            modifier = TODO()
                        )
                    }
                }
            }
        }
    }
}