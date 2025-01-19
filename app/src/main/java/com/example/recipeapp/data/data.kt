package com.example.recipeapp.data

// In a Kotlin file, e.g., DataStoreModule.kt
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.recipeapp.data.RecipeItem

val Context.MyRecipeDataStore: DataStore<Preferences> by preferencesDataStore(name = "MyRecipe")
val SAVED_RECIPES_KEY = stringPreferencesKey("MyRecipe_")


val Context.MyfavoriteRecipeDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")
val FAVORITES_KEY = stringPreferencesKey("favorite_personas")



suspend fun saveMyRecipes(context: Context, myRecipes: List<Recipe>) {
    try {
        val jsonString = Json.encodeToString(myRecipes)
        Log.d("SaveMyRecipes", "Serialized recipes: $jsonString")
        context.MyRecipeDataStore.edit { preferences ->
            preferences[SAVED_RECIPES_KEY] = jsonString
            println("Recipes saved to DataStore: $jsonString")
            Log.d("SaveMyRecipes", "Recipes saved successfully!")
        }
    } catch (e: Exception) {
        Log.e("SaveMyRecipes", "Error saving recipes: ${e.localizedMessage}")
    }
}


fun getMyRecipes(context: Context): Flow<List<Recipe>> {
    return context.MyRecipeDataStore.data.map { preferences ->
        val jsonString = preferences[SAVED_RECIPES_KEY] ?: "[]"
        println("Recipes fetched from DataStore: $jsonString")
        Log.d("GetMyRecipes", "Fetched recipes from DataStore: $jsonString")
        try {
            Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            Log.e("GetMyRecipes", "Error decoding recipes: ${e.message}")
            emptyList() // Return an empty list if decoding fails
        }
    }

}
    suspend fun saveFavorites(context: Context, favorites: List<RecipeItem>) {
        val jsonString = Json.encodeToString(favorites)
        context.MyfavoriteRecipeDataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = jsonString
            println("Saved Favorites: $jsonString") // Debug log
        }
    }

    fun getFavorites(context: Context): Flow<List<RecipeItem>> {
        return context.MyfavoriteRecipeDataStore.data
            .map { preferences ->
                val jsonString = preferences[FAVORITES_KEY] ?: ""
                println("Retrieved Favorites: $jsonString") // Debug log
                if (jsonString.isNotEmpty()) Json.decodeFromString(jsonString) else emptyList()
            }
    }









