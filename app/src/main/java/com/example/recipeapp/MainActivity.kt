package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipeapp.ui.theme.Recipe
import com.example.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                MainPage()
                }
            }
        }
    }


@Composable
fun MainPage() {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()) // Ensure no overlap with the status bar
                    .height(56.dp), // Adjust the height as per your design
                contentAlignment = Alignment.Center // Centers content inside the Box
            ) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineMedium, // Adjust font style
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            BottomBar()
        },
        content = { paddingValues ->
            // Add content like the search bar, categories, and recipes grid here
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                CustomSearchBar()
                chips()
                grids()
            }
        }
    )
}

@Preview (showBackground = true)
@Composable
fun CustomSearchBar() {
    OutlinedTextField(
        value = "", // Bind this to a state for user input
        onValueChange = { /* Handle text changes */ },
        placeholder = {
            Text(text = "Search for your wished recipes",)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp),

        shape = RoundedCornerShape(24.dp), // Rounded edges like in the image
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,             // Text color when focused
            unfocusedTextColor = Color.Black,           // Text color when not focused
            focusedPlaceholderColor = Color.Gray,       // Placeholder when focused
            unfocusedPlaceholderColor = Color.Gray,     // Placeholder when not focused
            focusedContainerColor = Color(0xFFE0E0E0),  // Gray background when focused
            unfocusedContainerColor = Color(0xFFE0E0E0), // Gray background when not focused
            cursorColor = Color.Black,                  // Black cursor
            focusedIndicatorColor = Color.Transparent,   // Remove underline when focused
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}



@Preview (showBackground = true)
@Composable
fun chips() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(rememberScrollState()) // Horizontal scrollable row
    ) {
        // Replace these with your actual category icons and labels
        listOf("Category1", "Category2", "Category3", "More...").forEach { category ->
            Button(onClick = { /* Handle category click */ }) {
                Text(text = category)
            }
        }
    }
}



@Preview (showBackground = true)
@Composable
fun grids() {
    val recipes = listOf("Crêpe", "Spaghetti", "Biryani", "Tacos") // Example list
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(recipes.size) { index ->
            // Replace with your actual recipe composable
            RecipeCard(recipeName = recipes[index])
        }
    }
}


@Composable
fun RecipeCard(recipeName: String) {
    @Composable
    fun RecipeCard(recipe: Recipe) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placeholder for recipe image (You can replace it with an actual image)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                // Display time, difficulty, and calories
                Text(
                    text = "Time: ${recipe.time}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Difficulty: ${recipe.difficulty}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Calories: ${recipe.calories}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBar() {
    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Setting") },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Favorites */ }
        )
        // Add more BottomNavigationItems for other sections like "Settings", etc.
    }
}



















