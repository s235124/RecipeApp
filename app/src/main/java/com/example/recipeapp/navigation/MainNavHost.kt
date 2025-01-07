package com.example.recipeapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            onRouteChanged(Route.RecipeDetailScreen)
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
            RecipeDetailScreen(navController, recipeName)
        }

        composable(Route.FavouritesScreen.title) {
            onRouteChanged(Route.FavouritesScreen)
            FavoritesScreen(navController,recipes)
        }

        composable(Route.MyRecipesScreen.title) {
            onRouteChanged(Route.MyRecipesScreen)
            MyRecipesScreen(navController, categories)
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
    }

}