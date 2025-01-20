package com.example.recipeapp.navigation


import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.screens.CategoryRecipesScreen
import com.example.recipeapp.screens.FavoritesScreen
import com.example.recipeapp.screens.MainScreen
import com.example.recipeapp.screens.RecipeDetailScreen
import com.example.recipeapp.screens.RecipeDetailsFromAPIScreen
import com.example.recipeapp.screens.SearchScreen
import com.example.recipeapp.screens.createmyscreen.CreateMyRecipe
import com.example.recipeapp.screens.recipe.MyRecipeViewModel
import com.example.recipeapp.screens.recipe.MyRecipeViewModelFactory
import com.example.recipeapp.screens.recipe.MyRecipesScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun MainNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onRouteChanged: (Route) -> Unit,
    modifier: Modifier = Modifier,
    favorites: MutableList<RecipeItem>,
    onSaveFavorites: (List<RecipeItem>) -> Unit,
    recipesFromAPI: RecipeAPI,
    categoriesFromAPI: CategoryAPI
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
                paddingValues = paddingValues,
                onCardClick = { recipe ->
                    navigateToAPIRecipeDetails(navController, recipe)
                },
                recipes = recipesFromAPI)
        }

        composable(
            "${Route.RecipeDetailFromAPIScreen.title}/{recipeJson}",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) // Slide in from the right
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) // Slide out to the left
            }
        ) { backStackEntry ->
            // Notify about route change
            onRouteChanged(Route.RecipeDetailFromAPIScreen)

            // Decode the recipe JSON
            val recipeJson = backStackEntry.arguments?.getString("recipeJson") ?: ""
            val format = Json { ignoreUnknownKeys = true }
            val recipe = recipeJson.let { format.decodeFromString<RecipeItem>(Uri.decode(it)) }
            val isInFav = favorites.contains(recipe)

            // Immediate background and content wrapper
            Box(modifier = Modifier.fillMaxSize()) {
                // Static background displayed immediately
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // Replace with your desired background color or image
                )

                // Main content that slides in
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
                    recipeExistsInFavourites = isInFav,
                )
            }
        }


        composable(
            "${Route.RecipeDetailScreen.title}/{recipeJson}",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) // Slide in from the right
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) // Slide out to the left
            }
        ) { backStackEntry ->
            onRouteChanged(Route.RecipeDetailScreen)
            val recipeJson = backStackEntry.arguments?.getString("recipeJson") ?: ""
            val format = Json { ignoreUnknownKeys = true }
            val recipe = recipeJson.let {format.decodeFromString<Recipe>(Uri.decode(it))}
            val context = LocalContext.current
            val viewModel: MyRecipeViewModel = viewModel(factory = MyRecipeViewModelFactory(context))
            val savedRecipes = viewModel.recipes.collectAsState().value
            val isInSavedRecipes = savedRecipes.contains(recipe)

            // Immediate background and content wrapper
            Box(modifier = Modifier.fillMaxSize()) {
                // Static background displayed immediately
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // Replace with your desired background color or image
                )
                RecipeDetailScreen(
                    innerPadding = paddingValues,
                    onBackButtonClick = { navController.popBackStack() },
                    recipe = recipe,
                    onDeleteClick = {
                        if (isInSavedRecipes) {
                            viewModel.deleteRecipe(recipe) // Remove from saved recipes
                        }
                        navController.popBackStack() // Navigate back
                    },
                    recipeCardExistsInMyRecipe = isInSavedRecipes
                )
            }
        }

        composable(Route.FavouritesScreen.title) {
            onRouteChanged(Route.FavouritesScreen)
            FavoritesScreen(
                favorites = favorites,
                onNavigateToRecipe = { recipe: RecipeItem ->
                    navigateToAPIRecipeDetails(navController, recipe)
                },
                onRemoveAllFavorites = {
                    favorites.clear()
                },
                padding = paddingValues
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
                },
                padding = paddingValues
            )
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

        composable(
            Route.CreateMyRecipeScreen.title,
            enterTransition = {
                slideInVertically(initialOffsetY = { fullHeight -> fullHeight })
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
            }
        ) {
            onRouteChanged(Route.CreateMyRecipeScreen)
            // Immediate background and content wrapper
            Box(modifier = Modifier.fillMaxSize()) {
                // Static background displayed immediately
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // Replace with your desired background color or image
                )
                CreateMyRecipe(
                    paddingValues = paddingValues,
                    onSaveClick = { navController.popBackStack() },
                    onCancelClick = { navController.popBackStack() }
                )
            }
        }
    }
}

fun navigateToAPIRecipeDetails(navController: NavHostController, recipe: RecipeItem) {
    val recipeJson = Uri.encode(Json.encodeToString(recipe))
    navController.navigate("${Route.RecipeDetailFromAPIScreen.title}/${recipeJson}")
}