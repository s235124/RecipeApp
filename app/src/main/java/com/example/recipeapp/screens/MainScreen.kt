package com.example.recipeapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipeapp.data.RecipeAPI
import com.example.recipeapp.data.RecipeItem
import com.example.recipeapp.model.Chips
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    onCardClick: (RecipeItem) -> Unit,
    onViewAllClick: (String) -> Unit,
    recipes: RecipeAPI,
    viewModel: CategoriesViewModel
) {
    val listState = rememberLazyListState()
    val categories by viewModel.categories.collectAsState()
    var pageNumber = 1

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
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

        items(categories.items) { catItem ->
            Chips(
                category = catItem,
                recipes = recipes,
                onCardClick = { recipe -> onCardClick(recipe) },
                onViewAllClick = { onViewAllClick(catItem.uid) }
            )
        }
    }

    // Detect end of list and trigger expansion
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()  // Prevent duplicate calls for the same item
            .collect { lastVisibleItemIndex ->
                val currentSize = categories.items.size
                Log.d("index", "${lastVisibleItemIndex.toString()} ; $currentSize")

                // Trigger pagination only when the user reaches the last batch of 5 items
                if (lastVisibleItemIndex != null && lastVisibleItemIndex / 5 >= pageNumber) {
                    pageNumber++
                    if (pageNumber <= 6) {
                        Log.d("Pagination", "Loading page $pageNumber")
                        viewModel.getCategory(pageNumber)
                    }
                }
            }
    }
}
