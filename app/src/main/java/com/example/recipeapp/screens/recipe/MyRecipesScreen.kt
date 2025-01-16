package com.example.recipeapp.screens.recipe

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.model.RecipeCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    onNewMyRecipeClick: () -> Unit,
    onNavigateToRecipeDetailScreen: (Recipe) -> Unit,

) {
    val context = LocalContext.current

    // Initialize com.example.recipeapp.screens.recipe.MyRecipeViewModel using the factory
    val viewModel: MyRecipeViewModel = viewModel(
        factory = MyRecipeViewModelFactory(context)
    )
    // Observe the saved recipes from the ViewModel
    val savedRecipes = viewModel.recipes.collectAsState().value

    // Debug logs for UI state
    LaunchedEffect(savedRecipes) {
        Log.d("MyRecipesScreen", "Saved recipes observed: $savedRecipes")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Recipes") },
                actions = {
                    IconButton(onClick = { onNewMyRecipeClick() }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add Recipe",
                            tint = Color(0xFF78B17E)
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (savedRecipes.isEmpty()) {
            // No recipes saved
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes saved yet.")
            }
        } else {
            // Display the saved recipes using RecipeCard
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(savedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onNavigateToRecipeDetailScreen = { onNavigateToRecipeDetailScreen(recipe) },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

class MyRecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyRecipeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
