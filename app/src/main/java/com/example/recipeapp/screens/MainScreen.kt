package com.example.recipeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.model.Chips
import com.example.recipeapp.data.CategoryAPI
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    onCardClick: (RecipeItem) -> Unit,
    onViewAllClick: () -> Unit,
    recipes: RecipeAPI,
    categories: CategoryAPI
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(
            start = 13.dp, //venstre side
            end = 13.dp, //højre side
            top = 20.dp,
            bottom = 80.dp), verticalArrangement = Arrangement.spacedBy(7.dp) //afstand fra hver recipe card
    ) {
        item {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Welcome",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 36.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }
            )
        }
        //categories section
        items(categories.items){ catItem ->
            Chips(
                category = catItem,
                recipes = recipes,
                onCardClick = { recipe -> onCardClick(recipe) },
                onViewAllClick = onViewAllClick
            )
        }
    }
}
