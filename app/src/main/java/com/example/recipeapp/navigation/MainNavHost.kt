package com.example.recipeapp.navigation

import CreateMyRecipe
import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.recipeapp.screens.RecipeDetailScreen
import com.example.recipeapp.screens.recipe.MyRecipesScreen
import com.example.recipeapp.screens.RecipeDetailsFromAPIScreen
import com.example.recipeapp.screens.SearchScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MainNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onRouteChanged: (Route) -> Unit,
    modifier: Modifier = Modifier,
    favorites: MutableList<RecipeItem>, //TODO: ADD THESE BACK LATER ON
    onSaveFavorites: (List<RecipeItem>) -> Unit,
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
                paddingValues = paddingValues,
                onCardClick = { recipe ->
                    navigateToAPIRecipeDetails(navController, recipe)
                },
                onViewAllClick = { categoryId ->
                    navController.navigate("${Route.CategoryRecipesScreen.title}/${categoryId}")
                },
                recipes = recipesFromAPI,
                categories = categoriesFromAPI
            )
        }

        composable(Route.SearchScreen.title) {
            onRouteChanged(Route.SearchScreen)

            SearchScreen(
                onCardClick = { recipe ->
                    navigateToAPIRecipeDetails(navController, recipe)
                },
                recipes = recipesFromAPI)
        }

        composable(
            "${Route.RecipeDetailFromAPIScreen.title}/{recipeJson}",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) // Slide in from the right
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }) // Slide out to the left
            }
        ) { backStackEntry ->
            onRouteChanged(Route.RecipeDetailFromAPIScreen)
            val recipeJson = backStackEntry.arguments?.getString("recipeJson") ?: ""
            val format = Json { ignoreUnknownKeys = true }
            val recipe = recipeJson.let {format.decodeFromString<RecipeItem>(Uri.decode(it))}
            var isInFav = favorites.contains(recipe)
            RecipeDetailsFromAPIScreen(
                innerPadding = paddingValues,
                onBackButtonClick = { navController.popBackStack() },
                recipe = recipe,
                onFavoriteClick = {
                    if(favorites.contains(recipe)){
                        favorites.remove(recipe)
                    } else {
                        favorites.add(recipe)
                    }
                    onSaveFavorites(favorites)
                },
                RecipeExistsInFavourites = isInFav,
            )
        }

        composable(
            "${Route.RecipeDetailScreen.title}/{recipeJson}",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) // Slide in from the right
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }) // Slide out to the left
            }
        ) { backStackEntry ->
            onRouteChanged(Route.RecipeDetailScreen)
            val recipeJson = backStackEntry.arguments?.getString("recipeJson") ?: ""
            val format = Json { ignoreUnknownKeys = true }
            val recipe = recipeJson.let {format.decodeFromString<Recipe>(Uri.decode(it))}
            RecipeDetailScreen(
                innerPadding = paddingValues,
                onBackButtonClick = { navController.popBackStack() },
                recipe = recipe
            )
        }

        composable(Route.FavouritesScreen.title) {
            onRouteChanged(Route.FavouritesScreen)
            FavoritesScreen(
                favorites = favorites,
                onNavigateToRecipe = { recipe: RecipeItem -> navigateToAPIRecipeDetails(navController, recipe)
                },

            )
        }


        composable(Route.MyRecipesScreen.title) {
            onRouteChanged(Route.MyRecipesScreen)
            MyRecipesScreen(
                onNewMyRecipeClick = {
                    navController.navigate(Route.CreateMyRecipeScreen.title)
                },
                onNavigateToRecipeDetailScreen = { recipe ->
                    val recipeJson = Uri.encode(Json.encodeToString(recipe))
                    navController.navigate("${Route.RecipeDetailScreen.title}/${recipeJson}")
                }
            )
        }

        composable(
            Route.AllCategoriesScreen.title,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 })// + fadeIn() // Slide in from the right
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 })// + fadeOut() // Slide out to the left
            }
        ) {
            onRouteChanged(Route.AllCategoriesScreen)
            AllCategoriesScreen(navController, categories)
        }

        composable("${Route.CategoryRecipesScreen.title}/{categoryId}") { backStackEntry ->
            onRouteChanged(Route.CategoryRecipesScreen)
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val category = categoriesFromAPI.items.filter { categoryItem -> categoryItem.uid == categoryId }[0]

            val filteredRecipes = recipesFromAPI.items.filter { item -> item.categoriesIds?.contains(categoryId) == true }

            CategoryRecipesScreen(
                paddingValues = paddingValues,
                onBackButtonClick = { navController.popBackStack() },
                category = category,
                onCardClick = {recipe -> navigateToAPIRecipeDetails(navController, recipe) },
                recipes = filteredRecipes,
            )
        }

        composable(Route.CreateMyRecipeScreen.title) {
            onRouteChanged(Route.CreateMyRecipeScreen)
            CreateMyRecipe(onSaveClick = { navController.popBackStack() })
        }
    }
}

fun navigateToAPIRecipeDetails(navController: NavHostController, recipe: RecipeItem) {
    val recipeJson = Uri.encode(Json.encodeToString(recipe))
    navController.navigate("${Route.RecipeDetailFromAPIScreen.title}/${recipeJson}")
}