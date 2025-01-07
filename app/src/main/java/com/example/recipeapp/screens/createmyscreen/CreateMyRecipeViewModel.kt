

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.saveMyRecipes
import com.example.recipeapp.data.getMyRecipes
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateMyRecipeViewModel(private val context: Context) : ViewModel() {

    // StateFlow to observe the list of recipes
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> get() = _recipes

    init {
        // Load the recipes from DataStore when the ViewModel is initialized
        viewModelScope.launch {
            _recipes.value = getMyRecipes(context).first()
        }
    }

    // Save a new recipe
    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                // Add the new recipe to the existing list
                val updatedRecipes = _recipes.value.toMutableList().apply { add(recipe) }
                // Save the updated list to DataStore
                saveMyRecipes(context, updatedRecipes)
                // Update the in-memory state
                _recipes.value = updatedRecipes
            } catch (e: Exception) {
                // Log or handle errors (optional)
            }
        }
    }
}
