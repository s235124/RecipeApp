package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.ui.theme.Recipe
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainPage(navController) }
                    composable("search/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        SearchResultsScreen(navController, query)
                    }
                }
            }
        }
    }
}


@Composable
fun MainPage(navController: NavController) {
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var recipes by remember { mutableStateOf(listOf<Recipe>()) }
    var categories by remember { mutableStateOf(listOf<String>()) }

    // Simulate API call on startup
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            recipes = fetchRecipes() // Fetch recipes from your API
            categories = fetchCategories() // Fetch categories from your API
        }
    }

    Scaffold(
        modifier = Modifier.background(color = Color.LightGray),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            BottomBar()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                CustomSearchBar(
                    searchQuery.value,
                    onSearchQueryChange = { query ->
                        searchQuery.value = query
                    },
                    onSearchButtonClick = {
                        navController.navigate("search/${searchQuery.value}")
                    }
                )
                Chips(categories) // Pass categories to the Chips function
                Grids(recipes) // Pass recipes to the Grids function
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController, query: String) {
    val coroutineScope = rememberCoroutineScope()
    var filteredRecipes by remember { mutableStateOf(listOf<Recipe>()) }

    LaunchedEffect(query) {
        coroutineScope.launch {
            val allRecipes = fetchRecipes() // Fetch all recipes
            filteredRecipes = if (query.isEmpty()) {
                allRecipes
            } else {
                allRecipes.filter { recipe ->
                    recipe.name.startsWith(query, ignoreCase = true)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Results") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(recipe = recipe)
                }
            }
        }
    )
}


@Composable
fun CustomSearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit, onSearchButtonClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(text = "Search for your wished recipes or categories")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedContainerColor = Color(0xFFE0E0E0),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onSearchButtonClick,
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Text(text = "Search")
        }
    }
}


@Composable
fun Chips(categories: List<String>) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                Button(onClick = { /* Handle category click */ }) {
                    Text(text = category)
                }
            }
        }
    }
}

@Composable
fun Grids(recipes: List<Recipe>) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Recipes",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(color = Color.LightGray)
        ) {
            items(recipes) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder for recipe image (You can replace it with an actual image)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            // Display time, difficulty, and calories
            Text(
                text = "Time: ${recipe.time}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Difficulty: ${recipe.difficulty}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Calories: ${recipe.calories}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


// Simulated functions to fetch recipes and categories
suspend fun fetchRecipes(): List<Recipe> {
    // Replace with actual API call
    return listOf(
        Recipe("Crêpe", "15 min", "Easy", "200 kcal"),
        Recipe("Spaghetti", "25 min", "Medium", "350 kcal"),
        Recipe("Biryani", "45 min", "Hard", "600 kcal"),
        Recipe("Tacos", "20 min", "Easy", "250 kcal")
    )
}

suspend fun fetchCategories(): List<String> {
    // Replace with actual API call
    return listOf("Italian", "Mexican", "Indian", "Desserts")
}

data class Recipe(
    val name: String,
    val time: String,
    val difficulty: String,
    val calories: String
)

@Composable
fun BottomBar() {
    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Setting") },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Favorites */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Setting") },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { /* Navigate to Favorites */ }
        )
        // Add more BottomNavigationItems for other sections like "Settings", etc.
    }
}



















