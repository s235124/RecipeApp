package com.example.recipeapp.screens.recipe

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.model.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    onNewMyRecipeClick: () -> Unit,
    onNavigateToRecipeDetailScreen: (Recipe) -> Unit,
    padding : PaddingValues
) {
    val context = LocalContext.current

    // Initialize MyRecipeViewModel using the factory
    val viewModel: MyRecipeViewModel = viewModel(
        factory = MyRecipeViewModelFactory(context)
    )
    // Observe the saved recipes from the ViewModel
    val savedRecipes = viewModel.recipes.collectAsState().value

    var deleteAllDialog by remember { mutableStateOf(false) }

    if (deleteAllDialog) {
        ShowDeleteAllRecipesPopUp(
            onDismiss = { deleteAllDialog = false },
            viewModel = viewModel
        )
    }

    // manual add top bar
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Recipes",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 36.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.bookmark_filled),
                        contentDescription = "My recipes",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF78B17E)
                    )
                },
                actions = {
                    // Existing Add Recipe Button
                    IconButton(onClick = { onNewMyRecipeClick() }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add Recipe",
                            tint = Color(0xFF78B17E),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Dropdown Menu Button
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color(0xFF78B17E),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Delete all recipes",
                                    color = Color.Red
                                )
                            },
                            onClick = {
                                expanded = false
                                deleteAllDialog = true
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Content area (LazyVerticalGrid)
        if (savedRecipes.isEmpty()) {
            // No recipes saved
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp), // avoid top bar overlap
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes saved yet.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp), //  avoid overlap wit topbar
                contentPadding = PaddingValues(8.dp)
            ) {
                items(savedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onNavigateToRecipeDetailScreen = { onNavigateToRecipeDetailScreen(recipe) },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShowDeleteAllRecipesPopUp(
    onDismiss: () -> Unit,
    viewModel: MyRecipeViewModel
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF78B17E),
        contentColor = Color.White,
        disabledContainerColor = Color(0xFFE8F5E9),
        disabledContentColor = Color(0xFFB0BEC5)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Warning") },
        text = { Text("Are you sure you want to delete all your recipes? This action is irreversible") },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.deleteAllRecipes()
                    onDismiss()
                },
                content = {
                    Text("Yes")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                content = {
                    Text("No")
                },
                colors = buttonColors
            )
        }
    )
}

class MyRecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyRecipeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
