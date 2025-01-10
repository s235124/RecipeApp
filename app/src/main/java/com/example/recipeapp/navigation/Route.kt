package com.example.recipeapp.navigation

sealed class Route (val title: String) {
    data object MainScreen : Route("main_screen")
    data object FavouritesScreen : Route("favourite_screen")
    data object MyRecipesScreen : Route("my_recipes_screen")
    data object RecipeDetailScreen : Route("recipe_details_screen")
    data object CategoryRecipesScreen : Route("categories_screen")
    data object AllCategoriesScreen : Route("all_categories_screen")
    data object SearchScreen : Route("search_screen")

}