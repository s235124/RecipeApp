package com.example.recipeapp.data

// In a Kotlin file, e.g., DataStoreModule.kt
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.util.Log

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MyRecipe")
val SAVED_RECIPES_KEY = stringPreferencesKey("MyRecipe_")



suspend fun saveMyRecipes(context: Context, myRecipes: List<Recipe>) {
    try {
        val jsonString = Json.encodeToString(myRecipes)
        Log.d("SaveMyRecipes", "Serialized recipes: $jsonString")
        context.dataStore.edit { preferences ->
            preferences[SAVED_RECIPES_KEY] = jsonString
            println("Recipes saved to DataStore: $jsonString")
            Log.d("SaveMyRecipes", "Recipes saved successfully!")
        }
    } catch (e: Exception) {
        Log.e("SaveMyRecipes", "Error saving recipes: ${e.localizedMessage}")
    }
}


fun getMyRecipes(context: Context): Flow<List<Recipe>> {
    return context.dataStore.data.map { preferences ->
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




