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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.compose.currentBackStackEntryAsState
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
                var categories by remember { mutableStateOf(emptyList<Category>()) }


                // Fetch recipes on startup
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        recipes = fetchRecipes()
                        categories = fetchCategories()
                    }
                }

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainPage(navController, recipes, categories) }
                    composable("search/{query}") {
                            backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        SearchResultsScreen(navController, query, recipes)
                    }
                    composable("recipeDetail/{recipeName}") { backStackEntry ->
                        val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
                        RecipeDetailScreen(navController, recipeName)
                    }
                    composable("Settings") {
                        SettingsScreen(navController)
                    }
                    composable("Favorites") {
                        FavoritesScreen(navController,recipes)
                    }
                    composable("MyRecipes") {
                        MyRecipesScreen(navController, categories)
                    }
                    composable("allCategories") {
                        AllCategoriesScreen(navController, categories)
                    }
                    composable("categoryRecipes/{categoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                        CategoryRecipesScreen(navController, categoryName, categories)
                    }
                }
            }
        }
    }
}


@Composable
fun MainPage(navController: NavController, recipes: List<Recipe>, categories: List<Category>) {
    val searchQuery = remember { mutableStateOf("") }

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

                Chips(
                    categories = categories,
                    onViewAllClick = {
                        // Handle "View All Categories" click, navigate to another screen if needed
                        navController.navigate("allCategories")
                    },
                    navController = navController
                )

                // Pass categories to the Chips function
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar ={ BottomBar(navController) },
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


@OptIn(ExperimentalMaterial3Api::class)
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF78B17E)
            )
        )
    }
}


@Composable
fun Chips(categories: List<Category>, navController: NavController, onViewAllClick: () -> Unit ) {
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
                        .clickable { navController.navigate("categoryRecipes/${category.name}")},
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
            // Add a "View All Categories" button at the end
            Text(
                text = "View All Categories",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF78B17E),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onViewAllClick() }
            )
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
                title = { Text("Recipe Details")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
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
fun SettingsScreen(navController: NavController) {
    val cardBackgroundColor = Color(0xFF78B17E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.padding(end = 8.dp).size(24.dp),
                            tint = Color(0xFF78B17E)
                        )
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                // Card for "Reset Favourites"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {/* popup */},
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().clickable { /* Handle click event */ }
                    ) {
                        Text(
                            text = "Reset Favourites",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Card for "Remove Your Recipes"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().clickable { /* Handle click event */ }
                    ) {
                        Text(
                            text = "Remove Your Recipes",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Card for "Support"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().clickable { /* Handle click event */ }
                    ) {
                        Text(
                            text = "Support",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Card for "About"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().clickable { /* Handle click event */ }
                    ) {
                        Text(
                            text = "About",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Version Text
                Text(
                    text = "Version: 0.0.1",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, recipes: List<Recipe>) {
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
    categories: List<Category>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Flatten all recipes from all categories
    val allRecipes = categories.flatMap { it.recipes }
    val filteredRecipes = allRecipes.filter { recipe ->
        (selectedCategory == "All" || recipe.categories == selectedCategory) &&
                recipe.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bookmark_filled),
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCategoriesScreen(navController: NavController, categories: List<Category>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Categories", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = 70.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("categoryRecipes/${category.name}") }
                        .padding(vertical = 5.dp)
                ) {
                    // Ensure flags are displayed properly
                    Box(
                        modifier = Modifier
                            .size(60.dp) // Increase the size for better visibility
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = category.flagResId),
                            contentDescription = "${category.name} flag",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(48.dp) // Ensure consistent flag size
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Display category name
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryRecipesScreen(
    navController: NavController,
    categoryName: String,
    categories: List<Category>
) {
    var searchQuery by remember { mutableStateOf("") }

    // Find the selected category and its recipes
    val category = categories.find { it.name.equals(categoryName, ignoreCase = true) }
    val categoryRecipes = category?.recipes ?: emptyList()

    // Filter recipes based on the search query
    val filteredRecipes = categoryRecipes.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$categoryName Recipes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Enhanced Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> searchQuery = query },
                placeholder = { Text("Search ${categoryName} recipes...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF0F0F0)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF78B17E)
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // Display filtered recipes
            if (filteredRecipes.isEmpty()) {
                Text(
                    text = "No recipes found",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
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
}




// Simulated functions to fetch recipes and categories
suspend fun fetchRecipes(): List<Recipe> {
    // Replace with actual API call
    val categories = fetchCategories() // Fetch categories with their associated recipes
    return categories.flatMap { it.recipes } // Flatten the list of recipes from all categories
}


suspend fun fetchCategories(): List<Category> {
    // Replace with actual API call
    return listOf(

        Category("Italy", R.drawable.flag_italy, recipes =  listOf(
            Recipe("Spaghetti", "25 min", "Medium", "350 kcal", R.drawable.oip, "Italy"),
            Recipe("Risotto", "40 min", "Hard", "500 kcal", R.drawable.oip,"Italy")
        )),
         Category("Lebanon", R.drawable.flag_lebanon, recipes =  listOf(
        Recipe("Hummus", "15 min", "Easy", "200 kcal", R.drawable.oip, "Lebanon"),
        Recipe("Tabbouleh", "30 min", "Medium", "150 kcal", R.drawable.oip, "Lebanon")
    )),
        Category("Pakistan", R.drawable.flag_pakistan, recipes = listOf(
        Recipe("Biryani", "45 min", "Hard", "600 kcal", R.drawable.oip,"Pakistan"),
        Recipe("Kebab", "30 min", "Medium", "400 kcal", R.drawable.oip,"Pakistan")
    ))
    )
}

data class Recipe(
    val name: String,
    val time: String,
    val difficulty: String,
    val calories: String,
    val imageRes: Int,
    val categories: String
)

data class Category(
    val name: String,
    val flagResId: Int,
    val recipes: List<Recipe>
)

@Composable
fun BottomBar(navController: NavController) {
//     Observe the current back stack entry to determine the current route
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color(0xFFDBDBDB)
    ) {
        val bottomcolor = Color(0xFF8FBC8F)

        NavigationBarItem(
            icon = {
                val icon = if (currentRoute == "main") Icons.Filled.Home else Icons.Outlined.Home
                Icon(icon, contentDescription = "Home", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Home") },
            selected = false,
            onClick = {
                navController.navigate("main") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentRoute == "MyRecipes") R.drawable.bookmark_filled else R.drawable.bookmark_outlined
                Icon(painterResource(icon), contentDescription = "My Recipes", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("My recipes") },
            selected = false,
            onClick = {
                navController.navigate("MyRecipes") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentRoute == "Favorites") Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                Icon(icon, contentDescription = "Favorites", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Favorites") },
            selected = false,
            onClick = {
                navController.navigate("Favorites") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentRoute == "Settings") Icons.Filled.Settings else Icons.Outlined.Settings
                Icon(icon, contentDescription = "Settings", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Settings") },
            selected = false,
            onClick = {
                navController.navigate("settings") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}