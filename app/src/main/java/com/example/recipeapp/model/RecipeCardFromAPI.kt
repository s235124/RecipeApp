package com.example.recipeapp.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
@Composable
fun RecipeCardFromAPI(
    recipe: RecipeItem,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier // Optional default modifier
) {
    val cardModifier: Modifier = if (modifier == Modifier) {
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clickable {
                onCardClick()
            }
    }
    else {
        modifier.clickable { onCardClick() }
    }
    val cardBackgroundColor = Color(0xFF78B17E)
    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)

    ) {
        Box(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier.padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = recipe.thumbnailUrl,
                    contentDescription = recipe.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Align to the bottom
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val timeText = buildString {
                    append("Time: ")
                    if (recipe.preparationTime != null && recipe.cookingTime != null) append("${recipe.preparationTime} + ${recipe.cookingTime}")
                    else if (recipe.preparationTime != null) append("${recipe.preparationTime}")
                    else if (recipe.cookingTime != null) append("${recipe.cookingTime}")
                    else {append("N/A"); return@buildString}
                    append(" minutes")
                }

                Text(
                    text = timeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )

                val properDifficulty = buildString {
                    when (recipe.difficult) {
                        "easy" -> append("Easy")
                        "moreEffort" -> append("More Effort")
                        "aChallenge" -> append("A Challenge")
                        else -> append("Unknown")
                    }
                }

                Text(
                    text = "Difficulty: $properDifficulty",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )

                val calories: String = recipe.kcal?.toString() ?: "Unknown"
                Text(
                    text = "Calories: $calories kcal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}
