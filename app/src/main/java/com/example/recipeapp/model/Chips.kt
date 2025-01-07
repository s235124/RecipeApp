package com.example.recipeapp.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.data.Category

@Composable
fun Chips(categories: List<Category>, onCategoryClick: (String) -> Unit, onViewAllClick: () -> Unit ) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onCategoryClick(category.name) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Display the flag with rounded corners
                    Image(
                        painter = painterResource(id = category.flagResId),
                        contentDescription = "${category.name} flag",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(16.dp)) // Rounded corners
                            .background(Color.White),

                        )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display the category name
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            // Add a "View All Categories" button at the end
            Text(
                text = "View All Categories",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF78B17E),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onViewAllClick() }
            )
        }
    }
}