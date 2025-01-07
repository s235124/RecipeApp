

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.getMyRecipes
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn

class MyRecipeViewModel(context: Context) : ViewModel() {

    // Recipes fetched from DataStore as a StateFlow
    val recipes: StateFlow<List<Recipe>> = getMyRecipes(context)
        .catch { e ->
            // Handle errors gracefully and provide an empty list as fallback
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
