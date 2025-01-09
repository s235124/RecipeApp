package com.example.recipeapp.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem

@Composable
fun RecipeCardFromAPI(
    recipe: RecipeItem,
    onCardClick: () -> Unit,
    modifier: Modifier
) {
    val cardBackgroundColor = Color(0xFF78B17E)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onCardClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)

    ) {
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
                    color = Color.White
            )
            var timeText = "Time: "

            if (recipe.preparationTime != null) timeText += "${recipe.preparationTime}"
            if (recipe.cookingTime != null) timeText += " + ${recipe.cookingTime}"

            timeText += " minutes"

            Text(
                text = timeText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Text(
                "Difficulty: ${recipe.difficult?.replaceFirstChar { it.uppercase() }?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            val calories: String = if (recipe.kcal == null) "Unknown"
            else recipe.kcal.toString()

            Text(
                "Calories: $calories kcal",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}