

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.getMyRecipes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyRecipeViewModel(context: Context) : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    init {
        viewModelScope.launch {
            try {


                getMyRecipes(context).collect { recipeList ->
                    _recipes.value = recipeList
                    println("Recipes in ViewModel: $recipeList")
                }
            }catch (e: Exception){
                println("Error fetching recipes: ${e.message}")
            }
        }
    }
}

