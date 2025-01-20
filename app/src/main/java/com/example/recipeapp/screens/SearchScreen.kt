package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.RecipeCardFromAPI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    onCardClick: (RecipeItem) -> Unit,
    recipes: RecipeAPI
) {
    val iconColor = Color(0xFF8FBC8F)
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes1 = recipes.items.filter { it.description.contains(searchQuery, ignoreCase = true) }
    var filteredRecipes2 = filteredRecipes1.filter { it.description.contains(searchQuery, ignoreCase = true) }
    val difficulties = remember { mutableStateOf(listOf(Pair("Easy", false), Pair("Effort", false), Pair("Challenge", false))) }
    val calories = remember { mutableStateOf(listOf(Pair("<200", false), Pair("200-400", false), Pair("400-600", false))) }
    val time = remember { mutableStateOf(listOf(Pair("<15 min", false), Pair("15-30 min", false), Pair(">30 min", false))) }

    var showFilters by remember { mutableStateOf(false) }

    fun filterRecipesByDifficulty() {
        // Start with the full list of recipes
        var difficultyFilter: List<RecipeItem>
        var calorieFilter = mutableListOf<RecipeItem>()
        var timeFilter = mutableListOf<RecipeItem>()
        var recipesToAdd: List<RecipeItem>
        timeFilter = filteredRecipes1.toMutableList()
        // first we check for any difficulty filters
        // Iterate through each difficulty and add recipes that match
        difficulties.value.forEach { (difficulty, isSelected) ->
            if (isSelected) {
                recipesToAdd = when (difficulty) {
                    // if a difficulty has been checked, we match it with any recipes in our list of filteredRecipes from the search query and add those recipes to recipesToAdd
                    "Easy" -> filteredRecipes1.filter { it.difficult?.contains("eas", ignoreCase = true) == true }
                    "Effort" -> filteredRecipes1.filter { it.difficult?.contains(difficulty, ignoreCase = true) == true }
                    "Challenge" -> filteredRecipes1.filter { it.difficult?.contains(difficulty, ignoreCase = true) == true }
                    else -> emptyList() //if no recipes found recipesToAdd is empty

                }
                timeFilter = emptyList<RecipeItem>().toMutableList() //if any difficulties were chosen, we empty timefilter, such that we only add the filteredRecipes from above
                calorieFilter.addAll(recipesToAdd)
                timeFilter.addAll(calorieFilter)
                recipesToAdd = emptyList() // we empty recipesToAdd for the next cycle
            }
        }

        if (calorieFilter.isEmpty())
            calorieFilter = filteredRecipes1.toMutableList()

        difficultyFilter = calorieFilter // we assign difficultyfilter to equate caloriefilter such that the next cycle only looks at the recipes we have so far

        calories.value.forEach { (calorieRange, isSelected) ->
            if (isSelected) {
                recipesToAdd = when (calorieRange) {
                    "<200" -> difficultyFilter.filter { it.kcal != null && it.kcal < 200 }
                    "200-400" -> difficultyFilter.filter { it.kcal != null && it.kcal in 200..400 }
                    "400-600" -> difficultyFilter.filter { it.kcal != null && it.kcal in 400..600 }
                    else -> emptyList()
                }

                timeFilter = emptyList<RecipeItem>().toMutableList() //empty timefilter first then we add the newly filtered recipes
                timeFilter.addAll(recipesToAdd)
            }
        }

        time.value.forEach { (time, isSelected) ->
            if (isSelected) {
                recipesToAdd = when (time) {
                    //we add cooking time with preparation time and then filter
                    "<15 min" -> timeFilter.filter {  (it.cookingTime ?: 0) + (it.preparationTime ?: 0) < 15 }
                    "15-30 min" -> timeFilter.filter  {  (it.cookingTime ?: 0) + (it.preparationTime ?: 0)  in 15..30 }
                    ">30 min" -> timeFilter.filter  {  (it.cookingTime ?: 0) + (it.preparationTime ?: 0) >30 }
                    else -> emptyList()
                }
                // rinse and repeat
                timeFilter = emptyList<RecipeItem>().toMutableList()
                timeFilter.addAll(recipesToAdd)
            }
        }


        // Update filteredRecipes1 with the new combined filtered list
        filteredRecipes1 = timeFilter

        filteredRecipes2 = timeFilter
    }
    filterRecipesByDifficulty()

    Column(modifier = Modifier.padding(paddingValues)) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Search Recipes",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 36.sp,
                    ),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = iconColor,
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        )

        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newQuery -> searchQuery = newQuery },
                placeholder = { Text("Search recipes...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            // Filter Button
            androidx.compose.material3.Button(
                onClick = { showFilters = !showFilters },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(if (showFilters) "Hide Filters" else "Filter")
            }
        }

        // Filters Section
        if (showFilters) {
            CheckboxWithDifficulties(filterList = difficulties, filterName = "Difficulty")
            CheckboxWithDifficulties(filterList = calories, filterName = "Calories")
            CheckboxWithDifficulties(filterList = time, filterName = "Time")

        }
        // Display filtered recipes
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            items(filteredRecipes2) { recipe ->
                RecipeCardFromAPI(
                    recipe = recipe,
                    onCardClick = { onCardClick(recipe) },
//                        modifier = Modifier
//                            .padding(horizontal = 8.dp)
//                            .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CheckboxWithDifficulties(
    filterList: MutableState<List<Pair<String, Boolean>>>, filterName: String
) {


    // State to track the checked state of each difficulty

    Text(
        text = filterName+":",
    )
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between checkboxes
            modifier = Modifier.fillMaxWidth().padding(8.dp) // Add padding for the entire row
        ) {
            // for each filter, create a checkbox
            filterList.value.forEachIndexed { index, (difficulty, isSelected) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { isChecked ->
                            // Update the state with the new checked value
                            filterList.value = filterList.value.toMutableList().apply {
                                this[index] = this[index].copy(second = isChecked)
                            }

                        }
                    )
                    Text(difficulty, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
