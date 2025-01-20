package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.CategoryItem
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.RecipeCardFromAPI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRecipesScreen(
    paddingValues: PaddingValues,
    onBackButtonClick: () -> Unit,
    onCardClick: (RecipeItem) -> Unit,
    category: CategoryItem,
    recipes: List<RecipeItem>,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = { Text(
                "${category.title.replace("&amp;", "&")} Recipes",
                fontWeight = FontWeight.Bold
            ) },
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Text(
            text = category.description.replace("&amp;", "&"),
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            items(recipes) { recipe ->
                RecipeCardFromAPI(
                    recipe = recipe,
                    onCardClick = { onCardClick(recipe) }
                )
            }
        }
    }
}