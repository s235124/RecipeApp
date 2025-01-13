package com.example.recipeapp.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.screens.AllCategoriesScreen
import com.example.recipeapp.screens.CategoryRecipesScreen
import com.example.recipeapp.screens.FavoritesScreen
import com.example.recipeapp.screens.MainScreen
import com.example.recipeapp.screens.MyRecipesScreen
import com.example.recipeapp.screens.RecipeDetailsFromAPIScreen
import com.example.recipeapp.screens.SearchScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MainNavHost(
    navController: NavHostController,
    onRouteChanged: (Route) -> Unit,
    modifier: Modifier = Modifier,
//    favorites: MutableList<Recipe>, TODO: ADD THESE BACK LATER ON
//    onSaveFavorites: (List<Recipe>) -> Unit,
    recipesFromAPI: RecipeAPI,
    categoriesFromAPI: CategoryAPI,
    recipes: List<Recipe>, // TODO: REMOVE WHEN RECIPES FROM API IS FULLY CHANGED IN ALL THE MODEL CLASSES
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
            MainScreen(
                onCardClick = { recipe ->
                    val recipeJson = Uri.encode(Json.encodeToString(recipe))
                    println(recipeJson)
                    navController.navigate("${Route.RecipeDetailScreen.title}/${recipeJson}")
                },
                onViewAllClick = {
                    navController.navigate(Route.AllCategoriesScreen.title)
                },
                recipes = recipesFromAPI,
                categories = categoriesFromAPI
            )
        }

        composable(Route.SearchScreen.title) {
            onRouteChanged(Route.SearchScreen)

            SearchScreen(
                onCardClick = { recipe ->
                    val recipeJson = Uri.encode(Json.encodeToString(recipe))
                    navController.navigate("${Route.RecipeDetailScreen.title}/${recipeJson}")
                },
                recipes = recipesFromAPI)
        }

        composable("${Route.RecipeDetailScreen.title}/{recipeJson}") { backStackEntry ->
            onRouteChanged(Route.RecipeDetailScreen)
            val recipeJson = backStackEntry.arguments?.getString("recipeJson") ?: ""
            val format = Json { ignoreUnknownKeys = true }
            val recipe = recipeJson.let {format.decodeFromString<RecipeItem>(Uri.decode(it))}
            RecipeDetailsFromAPIScreen(
                onBackButtonClick = { navController.popBackStack() },
                recipe = recipe
            )
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
            CategoryRecipesScreen(
                onBackButtonClick = TODO(),
                navController = TODO(),
                categoryName = categoryName,
                categories = TODO()
            )
        }
    }

}