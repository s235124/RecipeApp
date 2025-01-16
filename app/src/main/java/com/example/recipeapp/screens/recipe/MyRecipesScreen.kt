package com.example.recipeapp.screens.recipe

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    padding : PaddingValues
) {
    val context = LocalContext.current
    val viewModel: MyRecipeViewModel = viewModel(factory = MyRecipeViewModelFactory(context))
    // Observe the saved recipes from the ViewModel
    val savedRecipes = viewModel.recipes.collectAsState().value


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Recipes", fontWeight = FontWeight.Bold) },
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
    ) { paddingValues ->
        if (savedRecipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 70.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes saved yet.")
            }
        } else {
            // Display the saved recipes using RecipeCard
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 70.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Ens afstand mellem rækker
            )  {
                items(savedRecipes.chunked(2)) { recipePair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp), // Ens afstand mellem kort
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        recipePair.forEach { recipe->
                            RecipeCard(
                                recipe = recipe,
                                onNavigateToRecipeDetailScreen = { onNavigateToRecipeDetailScreen(recipe) },
                                modifier = Modifier
                                    .width(170.dp)
                            )
                        }

                        //Hvis kun er en opskrift i rækken, tilføj en tom Spacer for symmetri
                        if (recipePair.size < 2){
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
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
