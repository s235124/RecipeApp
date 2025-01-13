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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.navigation.MainNavHost
import com.example.recipeapp.navigation.Route
import com.example.recipeapp.screens.CategoriesViewModel
import com.example.recipeapp.screens.RecipeViewModel
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                // Fetch the recipes only once and share it across screens
                val coroutineScope = rememberCoroutineScope()

                var recipeAPI by remember { mutableStateOf(RecipeAPI()) }
                var categoriesAPI by remember { mutableStateOf(CategoryAPI()) }
                var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
                var categories by remember { mutableStateOf(emptyList<Category>()) }

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route
                val previousTab = navController.previousBackStackEntry?.destination?.route

                recipeAPI = fetchRecipesFromAPI()
                categoriesAPI = fetchCategoriesFromAPI()

                // Fetch recipes on startup
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        recipes = fetchRecipes()
                        categories = fetchCategories()
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
//                    val tags = fetchData()
                    MainNavHost(
                        paddingValues = innerPadding,
                        navController = navController,
                        onRouteChanged = { route ->
//                            Log.d("RecipeApp", "Navigated to route: ${route.title}")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        recipesFromAPI = recipeAPI,
                        categoriesFromAPI = categoriesAPI,
                        recipes = recipes,
                        categories = categories
                    )
                }
            }
        }
    }
}

@Composable
fun fetchRecipesFromAPI(): RecipeAPI {
    val viewModel: RecipeViewModel = viewModel() // Proper ViewModel instantiation
    val recipes by viewModel.data.collectAsState(initial = null) // Collect StateFlow

    println(recipes)

    return if (recipes == null) RecipeAPI()
    else recipes as RecipeAPI
}

@Composable
fun fetchCategoriesFromAPI(): CategoryAPI {
    val viewModel: CategoriesViewModel = viewModel() // Proper ViewModel instantiation
    val categories by viewModel.data.collectAsState(initial = null) // Collect StateFlow

    println(categories)

    return if (categories == null) CategoryAPI()
    else categories as CategoryAPI
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
            Recipe("Spaghetti", "25 min", "Medium", "350 kcal", R.drawable.oip, "Italy"),
            Recipe("Risotto", "40 min", "Hard", "500 kcal", R.drawable.oip,"Italy")
        )),
         Category("Lebanon", R.drawable.flag_lebanon, recipes =  listOf(
        Recipe("Hummus", "15 min", "Easy", "200 kcal", R.drawable.oip, "Lebanon"),
        Recipe("Tabbouleh", "30 min", "Medium", "150 kcal", R.drawable.oip, "Lebanon")
    )),
        Category("Pakistan", R.drawable.flag_pakistan, recipes = listOf(
        Recipe("Biryani", "45 min", "Hard", "600 kcal", R.drawable.oip,"Pakistan"),
        Recipe("Kebab", "30 min", "Medium", "400 kcal", R.drawable.oip,"Pakistan")
    ))
    )
}

//@Composable
//fun fetchData(): CategoryAPI? {
//    val viewModel: APITestingViewModel = viewModel() // Proper ViewModel instantiation
//    val recipeTags by viewModel.data.collectAsState(initial = null) // Collect StateFlow
//
////    println(recipeTags)
//    return recipeTags
//}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    currentTab: String?,
    previousTab: String?
) {
    val main = Route.MainScreen.title
    val allCategory = Route.AllCategoriesScreen.title
    val myRecipes = Route.MyRecipesScreen.title
    val favorites = Route.FavouritesScreen.title
    val search = Route.SearchScreen.title
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
                val icon = if (currentTab == search) Icons.Filled.Search else Icons.Outlined.Search
                Icon(icon, contentDescription = "Search", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Search") },
            selected = false,
            onClick = onSearchClick
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
    }
}