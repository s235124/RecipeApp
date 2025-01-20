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
    val filteredRecipes1 = recipes.items.filter { it.description.contains(searchQuery, ignoreCase = true) }

    val filteredRecipes2 = filteredRecipes1.filter { it.description.contains(searchQuery, ignoreCase = true) }
    val difficulties = listOf("Easy", "Medium", "Hard")
    val calories = listOf("<200", "200-400", "400-600")
    val time = listOf("<15 min", "15-30 min", ">30 min")
    val selectedDifficulties = remember { mutableStateOf(mutableSetOf<String>()) }
    var showFilters by remember { mutableStateOf(false) }
    val checkedStates = remember { mutableStateOf(difficulties.associateWith { false }.toMutableMap()) }


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
            CheckboxWithDifficulties(filterList = difficulties, filterName = "Time", checkedStates = checkedStates)
            CheckboxWithDifficulties(filterList = calories, filterName = "Time", checkedStates = checkedStates)
            CheckboxWithDifficulties(filterList = time, filterName = "Time", checkedStates = checkedStates)

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
    filterList: List<String>, filterName: String,
    checkedStates: MutableState<MutableMap<String, Boolean>>
) {
    // State to track the checked state of each difficulty

    Text(
        text = filterName+": ${
            checkedStates.value.filterValues { it }.keys.joinToString(", ")
        }",
    )
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between checkboxes
            modifier = Modifier.fillMaxWidth().padding(8.dp) // Add padding for the entire row
        ) {
            // for each filter, create a checkbox
            filterList.forEach { filter ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedStates.value[filter] ?: false,
                        onCheckedChange = { isChecked ->
                            checkedStates.value = checkedStates.value.toMutableMap().apply {
                                this[filter] = isChecked //
                            }
                        }
                    )
                    Text(
                        text = filter,
                        modifier = Modifier.padding(start = 8.dp) // Space between checkbox and text
                    )
                }
            }
        }

    }
}

