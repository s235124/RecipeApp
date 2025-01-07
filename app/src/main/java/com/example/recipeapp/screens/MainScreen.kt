package com.example.recipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.BottomBar
import com.example.recipeapp.model.Chips
import com.example.recipeapp.model.CustomSearchBar
import com.example.recipeapp.model.Grids
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.model.RecipeCard
import com.example.recipeapp.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, recipes: List<Recipe>, categories: List<Category>) {
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.background(color = Color.LightGray),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Welcome") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            //LazyColumn som den eneste scrollable container
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                //søgelinje som en separat item
                item{
                    CustomSearchBar(searchQuery.value,
                        onSearchQueryChange = {query ->
                            searchQuery.value = query
                        },
                        navController = navController
                    )
                }

                //categories-sektion som en separat item
                item{
                    Chips(categories = categories,
                        onCategoryClick = {name ->
                            navController.navigate("${Route.CategoryRecipesScreen.title}/${name}")
                        },
                        onViewAllClick = {
                            navController.navigate(Route.AllCategoriesScreen.title)
                        }
                    )
                }

                //suggestions-overskrift som item
                item{
                    Text(text = "Suggestions",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                //suggestions som lister i LazyColumn
                items(recipes){recipe ->
                    RecipeCard(recipe = recipe,
                        navController = navController,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )}
            }
        }
    )
}
