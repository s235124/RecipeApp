package com.example.recipeapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.data.Recipe


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(navController: NavController, recipe: Recipe) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (recipe.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = recipe.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Text(text = "Time: ${recipe.time}")
            Text(text = "Difficulty: ${recipe.difficulty}")
            Text(text = "Calories: ${recipe.calories}")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description:", style = MaterialTheme.typography.titleMedium)
            Text(text = recipe.description)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Ingredients:", style = MaterialTheme.typography.titleMedium)
            recipe.ingredient.forEach { ingredient ->
                Text(text = "- $ingredient")
            }
        }
    }
}