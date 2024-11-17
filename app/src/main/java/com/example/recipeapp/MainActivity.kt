package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.ui.theme.RecipeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()
                // Fetch the recipes only once and share it across screens
                val coroutineScope = rememberCoroutineScope()
                var recipes by remember { mutableStateOf(emptyList<Recipe>()) }

                // Fetch recipes on startup
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        recipes = fetchRecipes()
                    }
                }

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainPage(navController, recipes) }
                    composable("search/{query}") {
                            backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        SearchResultsScreen(navController, query, recipes)
                    }
                    composable("recipeDetail/{recipeName}") { backStackEntry ->
                        val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
                        RecipeDetailScreen(navController, recipeName)
                    }
                    composable("Setting") {
                        SettingScreen(navController)
                    }
                    composable("Favorites") {
                        favoritesScreen(navController,recipes)
                    }
                    composable("MyRecipes") {
                        MyRecipesScreen(navController, recipes)
                    }
                }
            }
        }
    }
}


@Composable
fun MainPage(navController: NavController, recipes: List<Recipe>) {
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var categories by remember { mutableStateOf(listOf<Category>()) }

    // Simulate API call on startup
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            categories = fetchCategories()// Fetch categories from your API
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
            BottomBar(navController = navController )
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
                    navController = navController
                )

                Chips(categories =categories , onCategoryClick = {

                }) // Pass categories to the Chips function
                Grids(
                    recipes,
                    navController = navController
                ) // Pass recipes to the Grids function
            }
        },
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController, query: String, recipes: List<Recipe>) {
    var searchQuery by remember { mutableStateOf("") }
    var filteredRecipes = recipes.filter { it.name.contains(searchQuery, ignoreCase = true) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Recipes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                    },
                    placeholder = { Text("Search recipes...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Display filtered recipes
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(filteredRecipes) { recipe ->
                        RecipeCard(
                            recipe = recipe, navController = navController, modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun CustomSearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit, navController: NavController, showNavigation: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                query -> onSearchQueryChange(query)

                // If showNavigation is true, navigate to search screen on query change
                if (showNavigation && query.isNotEmpty()) {
                    navController.navigate("search/$query")
                }

            },
            placeholder = {
                Text(text = "Search for your wished recipes or categories")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .shadow( elevation = 20.dp, RoundedCornerShape(10.dp), clip = true)

            ,
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
    }
}


@Composable
fun Chips(categories: List<Category>, onCategoryClick: (String) -> Unit ) {
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
                .padding(horizontal = 10.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onCategoryClick(category.name) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                        // Display the flag with rounded corners
                        Image(
                            painter = painterResource(id = category.flagResId),
                            contentDescription = "${category.name} flag",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(16.dp)) // Rounded corners
                                .background(Color.White),

                        )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display the category name
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun Grids(recipes: List<Recipe>, modifier: Modifier = Modifier, navController: NavController) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = "Suggestions",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier
                .fillMaxSize()
                .padding(8.dp)

        ) {
            items(recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    navController = navController,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, navController: NavController, modifier: Modifier) {
    val cardBackgroundColor = Color(0xFF78B17E)
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("recipeDetail/${recipe.name}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)

    ) {
        Column(
            modifier = Modifier.padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,


        ) {
            Image(
                painter = painterResource(id = recipe.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Time: ${recipe.time}", style = MaterialTheme.typography.bodySmall,color = Color.White)
            Text("Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodySmall,color = Color.White)
            Text("Calories: ${recipe.calories}", style = MaterialTheme.typography.bodySmall,color = Color.White)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(navController: NavController, recipeName: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Details") }
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recipe: $recipeName",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Example: Add more recipe details here
                Text(
                    text = "This is where the recipe details will go...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setting") }
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun favoritesScreen(navController: NavController, recipes: List<Recipe>) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRecipes = recipes.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorites",
                            modifier = Modifier.padding(end = 8.dp).size(24.dp),
                            tint = Color(0xFF78B17E)
                        )
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Search bar for filtering recipes
            CustomSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query -> searchQuery = query },
                navController = navController,
                showNavigation = false
            )

            // Updated LazyColumn with improved padding
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        navController = navController,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    navController: NavController,
    recipes: List<Recipe>
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredRecipes = recipes.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "My Recipes",
                            modifier = Modifier.padding(end = 8.dp).size(24.dp),
                            tint = Color(0xFF78B17E)
                        )
                        Text(
                            text = "My Recipes",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { /* Handle add recipe action */ }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Add Recipe",
                                modifier = Modifier.size(30.dp),
                                tint = Color(0xFF78B17E)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Search bar for filtering recipes
            CustomSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query -> searchQuery = query },
                navController = navController,
                showNavigation = false
            )

            // Updated LazyColumn with improved padding
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        navController = navController,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}


// Simulated functions to fetch recipes and categories
suspend fun fetchRecipes(): List<Recipe> {
    // Replace with actual API call
    return listOf(
        Recipe("Crêpe", "15 min", "Easy", "200 kcal",R.drawable.oip),
        Recipe("Spaghetti", "25 min", "Medium", "350 kcal",R.drawable.oip),
        Recipe("Biryani", "45 min", "Hard", "600 kcal",R.drawable.oip),
        Recipe("Tacos", "20 min", "Easy", "250 kcal",R.drawable.oip)
    )
}

suspend fun fetchCategories(): List<Category> {
    // Replace with actual API call
    return listOf(
        Category( "italy", R.drawable.flag_italy),
        Category("Lebanon", R.drawable.flag_lebanon),
        Category("Pakistan", R.drawable.flag_pakistan),
        Category("Turkey", R.drawable.flag_turkey),
        Category("Mexico", R.drawable.flag_mexico),
        Category("Somalia", R.drawable.somali_flag)
    )
}

data class Recipe(
    val name: String,
    val time: String,
    val difficulty: String,
    val calories: String,
    val imageRes: Int
)

data class Category(
    val name: String,
    val flagResId: Int
)

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.White
    ) {
        val bottomcolor= Color(0xFF8FBC8F)
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home",tint = bottomcolor ) },
            label = { Text("Home") },
            selected = false, // Handle selection logic
            onClick = { navController.navigate("main") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.List, contentDescription = "Setting",tint = bottomcolor) },
            label = { Text("My recipes") },
            selected = false, // Handle selection logic
            onClick = { navController.navigate("MyRecipes") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorites",tint = bottomcolor ) },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { navController.navigate("Favorites") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Settings, contentDescription = "Setting", tint = bottomcolor) },
            label = { Text("Favorites") },
            selected = false, // Handle selection logic
            onClick = { navController.navigate("setting") }
        )
        // Add more BottomNavigationItems for other sections like "Settings", etc.
    }
}


















