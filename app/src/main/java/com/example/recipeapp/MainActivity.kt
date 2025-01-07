package com.example.recipeapp

import CreateMyRecipe
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.data.Category
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.navigation.MainNavHost
import com.example.recipeapp.navigation.Route
import com.example.recipeapp.screens.AllCategoriesScreen
import com.example.recipeapp.screens.CategoryRecipesScreen
import com.example.recipeapp.screens.FavoritesScreen
import com.example.recipeapp.screens.MainScreen
import com.example.recipeapp.screens.MyRecipesScreen
import com.example.recipeapp.screens.RecipeDetailScreen
import com.example.recipeapp.screens.SearchResultsScreen
import com.example.recipeapp.screens.SettingsScreen
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

                Scaffold (
                    // TODO: FIGURE OUT WHAT TO DO WITH TOP BAR
                    bottomBar = {
                        BottomBar(
                            onHomeClick = {
                                navController.navigate(Route.MainScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onMyRecipesClick = {
                                navController.navigate(Route.MyRecipesScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onSettingsClick = {
                                navController.navigate(Route.SettingsScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            onFavoriteClick = {
                                navController.navigate(Route.FavouritesScreen.title) {
                                    launchSingleTop = true
                                }
                            },
                            currentTab = currentRoute,
                            previousTab = previousTab
                        )
                    }
                ) { innerPadding ->
                    MainNavHost(
                        navController = navController,
                        onRouteChanged = { route ->
//                            Log.d("RecipeApp", "Navigated to route: ${route.title}")
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        recipes = recipes,
                        categories = categories
                    )
                }
            }
        }
    }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Check if imageRes is available, else use imageUri
            if (recipe.imageRes != null) {
                Image(
                    painter = painterResource(id = recipe.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            } else if (recipe.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = Uri.parse(recipe.imageUri)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text("Time: ${recipe.time}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Difficulty: ${recipe.difficulty}", style = MaterialTheme.typography.bodySmall, color = Color.White)
            Text("Calories: ${recipe.calories}", style = MaterialTheme.typography.bodySmall, color = Color.White)
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
            Recipe("Spaghetti", "25 min", "Medium", "350 kcal", imageRes = R.drawable.oip, categories = "Italy"),
            Recipe("Risotto", "40 min", "Hard", "500 kcal", imageRes =  R.drawable.oip, categories = "Italy")
        )),
         Category("Lebanon", R.drawable.flag_lebanon, recipes =  listOf(
        Recipe("Hummus", "15 min", "Easy", "200 kcal", imageRes = R.drawable.oip, categories = "Lebanon"),
        Recipe("Tabbouleh", "30 min", "Medium", "150 kcal", imageRes = R.drawable.oip, categories =  "Lebanon")
    )),
        Category("Pakistan", R.drawable.flag_pakistan, recipes = listOf(
        Recipe("Biryani", "45 min", "Hard", "600 kcal", imageRes = R.drawable.oip, categories = "Pakistan"),
        Recipe("Kebab", "30 min", "Medium", "400 kcal", imageRes = R.drawable.oip, categories = "Pakistan")
    ))
    )
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onMyRecipesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    currentTab: String?,
    previousTab: String?
) {
    val main = Route.MainScreen.title
    val allCategory = Route.AllCategoriesScreen.title
    val myRecipes = Route.MyRecipesScreen.title
    val favorites = Route.FavouritesScreen.title
    val settings = Route.SettingsScreen.title
    val recipeDetails = Route.RecipeDetailScreen.title

    val recipeDetailScreenFromHome = currentTab?.contains(recipeDetails) == true && previousTab == main
    val recipeDetailScreenFromFavorites = currentTab?.contains(recipeDetails) == true && previousTab == favorites
    val recipeDetailScreenFromMyRecipes = currentTab?.contains(recipeDetails) == true && previousTab == myRecipes

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color(0xFFDBDBDB)
    ) {
        val bottomcolor = Color(0xFF8FBC8F)

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == main || currentTab == allCategory || recipeDetailScreenFromHome) Icons.Filled.Home else Icons.Outlined.Home
                Icon(icon, contentDescription = "Home", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Home") },
            selected = false,
            onClick = onHomeClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == myRecipes || recipeDetailScreenFromMyRecipes) R.drawable.bookmark_filled else R.drawable.bookmark_outlined
                Icon(painterResource(icon), contentDescription = "My Recipes", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("My recipes") },
            selected = false,
            onClick = onMyRecipesClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == favorites || recipeDetailScreenFromFavorites) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                Icon(icon, contentDescription = "Favorites", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Favorites") },
            selected = false,
            onClick = onFavoriteClick
        )

        NavigationBarItem(
            icon = {
                val icon = if (currentTab == settings) Icons.Filled.Settings else Icons.Outlined.Settings
                Icon(icon, contentDescription = "Settings", tint = bottomcolor, modifier = Modifier.size(32.dp))
            },
            label = { Text("Settings") },
            selected = false,
            onClick = onSettingsClick
        )
    }
}