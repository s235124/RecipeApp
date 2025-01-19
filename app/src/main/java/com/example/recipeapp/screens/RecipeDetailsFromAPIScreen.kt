package com.example.recipeapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.RecipeItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsFromAPIScreen(
    innerPadding: PaddingValues,
    onBackButtonClick: () -> Unit,
    recipe: RecipeItem,
    onFavoriteClick: (RecipeItem) -> Unit,
    RecipeExistsInFavourites: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Recipe Details") },
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { onFavoriteClick(recipe) }) {
                    val icon =
                        if (RecipeExistsInFavourites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                    Icon(imageVector = icon, contentDescription = "Favourite icon")
                }
            }
        )
        LazyColumn {
            // Image first
            item {
                AsyncImage(
                    model = recipe.thumbnailUrl,
                    contentDescription = recipe.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Making sure no nulls are included
            val timeText = buildString {
                if (recipe.preparationTime != null && recipe.cookingTime != null) append("${recipe.preparationTime} + ${recipe.cookingTime}")
                else if (recipe.preparationTime != null) append("${recipe.preparationTime}")
                else if (recipe.cookingTime != null) append("${recipe.cookingTime}")
                else {append("N/A"); return@buildString}
                append(" min")
            }

            val properDifficulty = buildString {
                when (recipe.difficult) {
                    "easy" -> append("Easy")
                    "moreEffort" -> append("More Effort")
                    "aChallenge" -> append("A Challenge")
                    else -> append("Unknown")
                }
            }

            val calories: String = if (recipe.kcal == null) "Unknown"
            else recipe.kcal.toString()

            // Small descriptive text
            item {
                Text(
                    text = "$timeText | $properDifficulty | $calories kcal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Description
            item {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = recipe.description.replace("�", "% "),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Ingredients
            item {
                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                for (ingredient in recipe.ingredients!!) {
                    Text(
                        text = "• ${ingredient.description.replace("�", "% ")}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Method
            item {
                Text(
                    text = "Method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                var i = 1

                for (step in recipe.steps) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Step $i",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = step.replace("�", "% "),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    i++
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Nutritional facts
            item {
                Text(
                    text = "Nutrition ${recipe.nutrition}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Calories: ${recipe.kcal.toString()} kcal",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Fat: ${recipe.fat.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Saturates: ${recipe.saturates.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Carbs: ${recipe.carbs.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Sugars: ${recipe.sugars.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Fibre: ${recipe.fibre.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Protein: ${recipe.protein.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "Salt: ${recipe.salt.toString()} grams",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}