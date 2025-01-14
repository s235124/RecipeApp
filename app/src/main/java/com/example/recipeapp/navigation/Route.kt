package com.example.recipeapp.navigation

sealed class Route (val title: String) {
    data object MainScreen : Route("main_screen")
    data object FavouritesScreen : Route("favourite_screen")
    data object MyRecipesScreen : Route("my_recipes_screen")
    data object RecipeDetailScreen : Route("recipe_details_screen")
    data object CategoryRecipesScreen : Route("categories_screen")
    data object AllCategoriesScreen : Route("all_categories_screen")
    data object SearchResultScreen : Route("search_results_screen")
    data object SettingsScreen : Route("settings_screen")
    data object CreateMyRecipe: Route("create_my_recipe")
    data object SearchScreen : Route("search_screen")

}