package com.example.recipeapp.model



import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe


@Composable
fun RecipeCard(
    recipe: Recipe,
    onNavigateToRecipeDetailScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackgroundColor = Color(0xFF78B17E)



    Card(
        modifier = modifier.clickable {
            onNavigateToRecipeDetailScreen()
        },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Check if imageRes is available; otherwise, use imageUri
            if (recipe.imageRes != null) {
                Image(
                    painter = painterResource(id = recipe.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            } else if (recipe.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = Uri.parse(recipe.imageUri)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
            else {
                Image(
                    painter = painterResource(R.drawable.oip),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Time: ${recipe.time}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Calories: ${recipe.calories}", style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
    }
}
