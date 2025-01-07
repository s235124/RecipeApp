package com.example.recipeapp

import CreateMyRecipe
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.saveSampleDataToDataStore
import com.example.recipeapp.navigation.MainNavHost
import com.example.recipeapp.navigation.Route
import com.example.recipeapp.screens.AllCategoriesScreen
import com.example.recipeapp.screens.CategoryRecipesScreen
import com.example.recipeapp.screens.FavoritesScreen
import com.example.recipeapp.screens.MainScreen
import com.example.recipeapp.screens.MyRecipesScreen
import com.example.recipeapp.screens.RecipeDetailScreen
import com.example.recipeapp.screens.SearchResultsScreen
import com.example.recipeapp.screens.SettingsScreen
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            saveSampleDataToDataStore(applicationContext)
        }
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                // Fetch the recipes only once and share it across screens
                val coroutineScope = rememberCoroutineScope()

                var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
                var categories by remember { mutableStateOf(emptyList<Category>()) }


                val previousTab = navController.previousBackStackEntry?.destination?.route
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                // Fetch recipes on startup
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        recipes = fetchRecipes()
                        categories = fetchCategories()
                    }
                }

                Scaffold (
                    // TODO: FIGURE OUT WHAT TO DO WITH TOP BAR
                    bottomBar = {
                        BottomBar(
                            onHomeClick = {
                                navController.navigate(Route.MainScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onMyRecipesClick = {
                                navController.navigate(Route.MyRecipesScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onSettingsClick = {
                                navController.navigate(Route.SettingsScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onFavoriteClick = {
                                navController.navigate(Route.FavouritesScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            currentTab = currentRoute,
                            previousTab = previousTab
                        )
                    }
                ) { innerPadding ->
                    MainNavHost(
                        navController = navController,
                        onRouteChanged = { route ->
//                            Log.d("RecipeApp", "Navigated to route: ${route.title}")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        recipes = recipes,
                        categories = categories
                    )
                }
            }
        }
    }
}




// Simulated functions to fetch recipes and categories
suspend fun fetchRecipes(): List<Recipe> {
    // Replace with actual API call
    val categories = fetchCategories() // Fetch categories with their associated recipes
    return categories.flatMap { it.recipes } // Flatten the list of recipes from all categories
}


suspend fun fetchCategories(): List<Category> {
    // Replace with actual API call
    return listOf(

        Category("Italy", R.drawable.flag_italy, recipes =  listOf(
            Recipe("Spaghetti", "25 min", "Medium", "350 kcal", imageRes = R.drawable.oip, categories = "Italy"),
            Recipe("Risotto", "40 min", "Hard", "500 kcal", imageRes =  R.drawable.oip, categories = "Italy")
        )),
         Category("Lebanon", R.drawable.flag_lebanon, recipes =  listOf(
        Recipe("Hummus", "15 min", "Easy", "200 kcal", imageRes = R.drawable.oip, categories = "Lebanon"),
        Recipe("Tabbouleh", "30 min", "Medium", "150 kcal", imageRes = R.drawable.oip, categories =  "Lebanon")
    )),
        Category("Pakistan", R.drawable.flag_pakistan, recipes = listOf(
        Recipe("Biryani", "45 min", "Hard", "600 kcal", imageRes = R.drawable.oip, categories = "Pakistan"),
        Recipe("Kebab", "30 min", "Medium", "400 kcal", imageRes = R.drawable.oip, categories = "Pakistan")
    ))
    )
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    currentTab: String?,
    previousTab: String?
) {
    val main = Route.MainScreen.title
    val allCategory = Route.AllCategoriesScreen.title
    val myRecipes = Route.MyRecipesScreen.title
    val favorites = Route.FavouritesScreen.title
    val settings = Route.SettingsScreen.title
    val recipeDetails = Route.RecipeDetailScreen.title

    val recipeDetailScreenFromHome = currentTab?.contains(recipeDetails) == true && previousTab == main
    val recipeDetailScreenFromFavorites = currentTab?.contains(recipeDetails) == true && previousTab == favorites
    val recipeDetailScreenFromMyRecipes = currentTab?.contains(recipeDetails) == true && previousTab == myRecipes

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color(0xFFDBDBDB)
    ) {
        val bottomcolor = Color(0xFF8FBC8F)

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == main || currentTab == allCategory || recipeDetailScreenFromHome) Icons.Filled.Home else Icons.Outlined.Home
                Icon(icon, contentDescription = "Home", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Home") },
            selected = false,
            onClick = onHomeClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == myRecipes || recipeDetailScreenFromMyRecipes) R.drawable.bookmark_filled else R.drawable.bookmark_outlined
                Icon(painterResource(icon), contentDescription = "My Recipes", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("My recipes") },
            selected = false,
            onClick = onMyRecipesClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == favorites || recipeDetailScreenFromFavorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                Icon(icon, contentDescription = "Favorites", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Favorites") },
            selected = false,
            onClick = onFavoriteClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == settings) Icons.Filled.Settings else Icons.Outlined.Settings
                Icon(icon, contentDescription = "Settings", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Settings") },
            selected = false,
            onClick = onSettingsClick
        )
    }
}