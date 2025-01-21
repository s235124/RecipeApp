package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.data.getFavorites
import com.example.recipeapp.data.saveFavorites
import com.example.recipeapp.navigation.MainNavHost
import com.example.recipeapp.navigation.Route
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                // Fetch the recipes only once and share it across screens
                val coroutineScope = rememberCoroutineScope()

                val favorites = remember { mutableStateListOf<RecipeItem>() }
                val context = LocalContext.current


                val previousTab = navController.previousBackStackEntry?.destination?.route
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                // Fetch recipes on startup
                LaunchedEffect(Unit) {
                    getFavorites(context).collectLatest { savedFavorites ->
                        favorites.clear()
                        favorites.addAll(savedFavorites)
                    }
                }

                Scaffold (
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
                            onSearchClick = {
                                navController.navigate(Route.SearchScreen.title) {
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
                        paddingValues = innerPadding,
                        navController = navController,
                        onRouteChanged = { route ->
//                            Log.d("RecipeApp", "Navigated to route: ${route.title}")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        favorites = favorites,
                        onSaveFavorites = { updatedFavorite ->
                            coroutineScope.launch {
                                saveFavorites(context, updatedFavorite)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    onHomeClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    currentTab: String?,
    previousTab: String?
) {
    val main = Route.MainScreen.title
    val myRecipes = Route.MyRecipesScreen.title
    val favorites = Route.FavouritesScreen.title
    val search = Route.SearchScreen.title
    val recipeDetailsFromAPI = Route.RecipeDetailFromAPIScreen.title
    val recipeDetails = Route.RecipeDetailScreen.title
    val create = Route.CreateMyRecipeScreen.title

    val recipeDetailScreenFromHome = currentTab?.contains(recipeDetailsFromAPI) == true && previousTab == main
    val recipeDetailScreenFromFavorites = currentTab?.contains(recipeDetailsFromAPI) == true && previousTab == favorites
    val recipeDetailScreenFromMyRecipes = currentTab?.contains(recipeDetails) == true && previousTab == myRecipes

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color(0xFFDBDBDB)
    ) {
        val bottomcolor = Color(0xFF8FBC8F)

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == main || recipeDetailScreenFromHome) Icons.Filled.Home else Icons.Outlined.Home
                Icon(icon, contentDescription = "Home", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Home") },
            selected = false,
            onClick = onHomeClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == search) Icons.Filled.Search else Icons.Outlined.Search
                Icon(icon, contentDescription = "Search", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Search") },
            selected = false,
            onClick = onSearchClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == myRecipes || currentTab == create || recipeDetailScreenFromMyRecipes) R.drawable.bookmark_filled else R.drawable.bookmark_outlined
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
    }
}