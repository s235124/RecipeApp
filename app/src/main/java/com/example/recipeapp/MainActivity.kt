package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    // Your implementation for the chips
}
@Preview (showBackground = true)
@Composable
fun grids() {
    // Your implementation for the grids
}

@Preview (showBackground = true)
@Composable
fun BottomBar() {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Home */ }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Favorites */ }
        )
        // Add more navigation items for "My Recipes", "Settings", etc.
    }
}

@Composable
fun BottomNavigationItem(icon: @Composable () -> Unit, label: @Composable () -> Unit, selected: Boolean, onClick: () -> Unit) {

}

@Composable
fun BottomNavigation(content: @Composable () -> Unit) {

}



