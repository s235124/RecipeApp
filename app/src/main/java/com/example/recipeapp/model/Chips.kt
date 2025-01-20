package com.example.recipeapp.model

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.recipeapp.data.CategoryItem
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem

@Composable
fun Chips(
    category: CategoryItem,
    recipes: RecipeAPI,
    onCardClick: (RecipeItem) -> Unit,
    onViewAllClick: () -> Unit
) {
    val categoryRecipes = recipes.items.filter { recipe -> recipe.categoriesIds?.contains(category.uid) == true }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = category.title.replace("&amp;", "&"),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = category.description.replace("&amp;", "&"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var i = 0
            categoryRecipes.forEach { recipe ->
                if (i >= 5) return@forEach
                RecipeCardFromAPI(
                    recipe = recipe,
                    onCardClick = { onCardClick(recipe) },
                    modifier = Modifier.width(150.dp).height(300.dp)
                )
                Spacer (modifier = Modifier.width(16.dp))
                i++
            }
            // Add a "View All Recipes" button at the end
            Button(
                onClick = onViewAllClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF78B17E), // Lighter green color
                    contentColor = Color.White,   // Darker green for text
                    disabledContainerColor = Color(0xFFE8F5E9), // Optional: Lighter green for disabled state
                    disabledContentColor = Color(0xFFB0BEC5)    // Optional: Grayish text for disabled state
                )
            ) {
                Text(
                    text = "View All\nRecipes",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White, // Match the text color to the contentColor of the button
                    modifier = Modifier
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.width(4.dp))
        }
    }
}