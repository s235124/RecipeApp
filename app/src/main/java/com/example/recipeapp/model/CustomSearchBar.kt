package com.example.recipeapp.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CustomSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navController: NavController,
    showNavigation: Boolean = true,
    paddingValues: PaddingValues
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .height(50.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                onSearchQueryChange(query)

                // If showNavigation is true, navigate to search screen on query change
                if (showNavigation && query.isNotEmpty()) {

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
                .shadow(elevation = 20.dp, RoundedCornerShape(10.dp), clip = true),
            shape = RoundedCornerShape(24.dp),
            /*colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF78B17E)
            )*/
        )
    }
}