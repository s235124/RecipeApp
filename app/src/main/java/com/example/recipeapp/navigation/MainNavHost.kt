package com.example.recipeapp.navigation

import CreateMyRecipe
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.screens.AllCategoriesScreen
import com.example.recipeapp.screens.CategoryRecipesScreen
import com.example.recipeapp.screens.FavoritesScreen
import com.example.recipeapp.screens.MainScreen
import com.example.recipeapp.screens.MyRecipesScreen
import com.example.recipeapp.screens.RecipeDetailScreen
import com.example.recipeapp.screens.SearchResultsScreen
import com.example.recipeapp.screens.SettingsScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    onRouteChanged: (Route) -> Unit,
    modifier: Modifier = Modifier,
//    favorites: MutableList<Recipe>, TODO: ADD THESE BACK LATER ON
//    onSaveFavorites: (List<Recipe>) -> Unit,
    recipes: List<Recipe>,
    categories: List<Category>
) {
    NavHost(
        navController = navController,
        startDestination = Route.MainScreen.title,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Route.MainScreen.title) {
            onRouteChanged(Route.MainScreen)
            MainScreen(navController, recipes, categories)
        }

        composable("${Route.SearchResultScreen.title}/{query}") { backStackEntry ->
            onRouteChanged(Route.SearchResultScreen)
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(navController, query, recipes)
        }

        composable("${Route.RecipeDetailScreen.title}/{recipeName}") { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
            val recipe = recipes.find { it.name == recipeName }
            if (recipe != null) {
               // Route.RecipeDetailScreen(navController = navController, recipe = recipe)
            } else {
                Text("Recipe not found!", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
        }

        composable(Route.FavouritesScreen.title) {
            onRouteChanged(Route.FavouritesScreen)
            FavoritesScreen(navController,recipes)
        }

        composable(Route.MyRecipesScreen.title) {
            onRouteChanged(Route.MyRecipesScreen)
            MyRecipesScreen(navController)
        }

        composable(Route.AllCategoriesScreen.title) {
            onRouteChanged(Route.AllCategoriesScreen)
            AllCategoriesScreen(navController, categories)
        }

        composable("${Route.CategoryRecipesScreen.title}/{categoryName}") { backStackEntry ->
            onRouteChanged(Route.CategoryRecipesScreen)
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryRecipesScreen(navController, categoryName, categories)
        }

        composable(Route.SettingsScreen.title) {
            onRouteChanged(Route.SettingsScreen)
            SettingsScreen(navController)
        }
        composable(Route.CreateMyRecipe.title) {
            onRouteChanged(Route.CreateMyRecipe)
            CreateMyRecipe(navController)
        }
    }

}