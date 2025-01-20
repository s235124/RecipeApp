package com.example.recipeapp.screens

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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.RecipeCardFromAPI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<RecipeItem>,
    onNavigateToRecipe: (RecipeItem) -> Unit,
    onRemoveAllFavorites: () -> Unit,
    padding : PaddingValues
) {
    var removeAllDialog by remember { mutableStateOf(false) }

    if (removeAllDialog) {
        ShowRemoveAllRecipesPopUp(
            onDismiss = { removeAllDialog = false },
            onRemoveAllFavorites = onRemoveAllFavorites
        )
    }
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Favorites",
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
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorites",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF78B17E)
                    )
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
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
                                    text = "Remove all recipes",
                                    color = Color.Red
                                )
                            },
                            onClick = {
                                expanded = false
                                removeAllDialog = true
                            }
                        )
                    }
                }
            )
        }
        if (favorites.isEmpty()) {
            // No recipes saved
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp), // avoid top bar overlap
                contentAlignment = Alignment.Center
            ) {
                Text("No recipes favorited yet.")
            }
        }
        else {
            LazyVerticalGrid (
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(favorites) { recipe ->
                    RecipeCardFromAPI(
                        recipe = recipe,
                        onCardClick = { onNavigateToRecipe(recipe) }
                    )
                }
            }
        }
    }
}

@Composable
fun ShowRemoveAllRecipesPopUp(
    onDismiss: () -> Unit,
    onRemoveAllFavorites: () -> Unit
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
        text = { Text("Are you sure you want to remove all your favorited recipes? This action is irreversible") },
        confirmButton = {
            Button(
                onClick = {
                    onRemoveAllFavorites()
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