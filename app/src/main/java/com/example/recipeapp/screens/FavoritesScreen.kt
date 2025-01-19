package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.RecipeCardFromAPI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<RecipeItem>,
    onNavigateToRecipe: (RecipeItem) -> Unit,
    padding : PaddingValues
) {
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Favorites",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorites",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF78B17E)
                    )
                }
            )
        }
        if (favorites.isEmpty()) {
            // No recipes saved
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp), // avoid top bar overlap
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes favorited yet.")
            }
        }
        else {
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(favorites) { recipe ->
                    RecipeCardFromAPI(
                        recipe = recipe,
                        onCardClick = { onNavigateToRecipe(recipe) }
                    )
                }
            }
        }
    }
}





