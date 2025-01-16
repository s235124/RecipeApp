package com.example.recipeapp.screens.createmyscreen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.data.Recipe

@Composable
fun CreateMyRecipe(
    paddingValues: PaddingValues,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF78B17E),
        contentColor = Color.White,
//        disabledContainerColor = Color(0xFFE8F5E9),
        disabledContentColor = Color(0xFFB0BEC5)
    )

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF78B17E),
        unfocusedContainerColor = Color(0xFFE8F5E9)
    )

    val context = LocalContext.current
    val viewModel: CreateMyRecipeViewModel =
        viewModel(factory = CreateMyRecipeViewModelFactory(context))

    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("Select Time") }
    var difficulty by remember { mutableStateOf("Select Difficulty") }
    var calories by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }  // List to hold ingredients
    var newIngredient by remember { mutableStateOf("") }  // To hold the current input for new ingredient
    var method by remember { mutableStateOf(listOf<String>()) }
    var step by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val savedUri by viewModel.imageUriFlow.collectAsState()

    // Restore saved image URI from ViewModel or DataStore
//    LaunchedEffect(savedUri) {
//        if (savedUri != null) {
//            imageUri = Uri.parse(savedUri)
//        }
//    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            viewModel.saveImageUri(uri.toString()) // Save image URI to DataStore
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { selectedTime ->
                time = selectedTime
            }
        )
    }

    if (showDifficultyDialog) {
        ShowDifficultyDialog(
            onDismiss = { showDifficultyDialog = false },
            onDifficultySelected = { selectedDifficulty ->
                difficulty = selectedDifficulty
            }
        )
    }

    //---------------------------

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Selection
            item {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = buttonColors
                    ) {
                        Text("Select Image")
                    }
                }
            }

            // Title of recipe
            item {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Recipe Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            }

            // Time picker
            item {
                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors
                ) {
                    Text(time) // Shows selected time or default text
                }
            }

            // Difficulty button
            item {
                Button(
                    onClick = { showDifficultyDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors
                ) {
                    Text(difficulty)
                }
            }

            // Calories
            item {
                TextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            }

            // Description
            item {
                MultiLineTextField(
                    text = description,
                    placeholder = "Write a description for your recipe...",
                    onTextChange = { description = it }
                )
            }

            // Ingredients
            item {
                Column {
                    var buttonEnabled by remember { mutableStateOf(false) }
                    // Ingredient Input Field
                    TextField(
                        value = newIngredient,
                        onValueChange = {
                            newIngredient = it; buttonEnabled = newIngredient.isNotEmpty()
                        },
                        label = { Text("New Ingredient") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    // Add Ingredient Button
                    Button(
                        onClick = {
                            if (newIngredient.isNotEmpty()) {
                                ingredients =
                                    ingredients + newIngredient  // Add new ingredient to the list
                                newIngredient = ""  // Clear input field after adding
                                buttonEnabled = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = buttonEnabled,
                        colors = buttonColors
                    ) {
                        Text(
                            text = "Add Ingredient"
                        )
                    }

                    // Displaying the list of added ingredients
                    if (ingredients.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ingredients.forEachIndexed { index, ingredient ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(ingredient, modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = {
                                            ingredients =
                                                ingredients.toMutableList()
                                                    .apply { removeAt(index) }
                                        },
                                        colors = buttonColors
                                    ) {
                                        Text(
                                            text = "Delete",
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Method
            item {
                Column {
                    var buttonEnabled by remember { mutableStateOf(false) }
                    // Step Input Field
                    TextField(
                        value = step,
                        onValueChange = { step = it; buttonEnabled = step.isNotEmpty() },
                        label = { Text("New Step") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    // Add Ingredient Button
                    Button(
                        onClick = {
                            if (step.isNotEmpty()) {
                                method = method + step  // Add new ingredient to the list
                                step = ""  // Clear input field after adding
                                buttonEnabled = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = buttonEnabled,
                        colors = buttonColors
                    ) {
                        Text(
                            text = "Add Step"
                        )
                    }

                    // Displaying the list of added ingredients
                    if (method.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            method.forEachIndexed { index, step ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(step, modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = {
                                            method =
                                                method.toMutableList().apply { removeAt(index) }
                                        },
                                        colors = buttonColors
                                    ) {
                                        Text(
                                            text = "Delete",
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Save Button
            item {
                Button(
                    onClick = {
                        viewModel.saveRecipe(
                            Recipe(
                                name = name,
                                time = time,
                                difficulty = difficulty,
                                calories = calories,
                                description = description,
                                imageUri = imageUri?.toString(),
                                categories = "Custom",
                                ingredient = ingredients,
                                method = method
                            )
                        )
                        onSaveClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors
                ) {
                    Text("Save Recipe")
                }
            }
        }

        FloatingActionButton(
            onClick = { onCancelClick() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .size(32.dp),
            containerColor = Color.Red,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel"
            )
        }
    }
}


@Composable
fun MultiLineTextField(
    text: String,
    placeholder: String,
    onTextChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Multi-line input height
            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
            .padding(8.dp),
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (text.isEmpty()) {
                    Text(text = placeholder, color = Color.Gray)
                }
                innerTextField()
            }
        }
    )
}

class CreateMyRecipeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateMyRecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateMyRecipeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//Selveste rullehjulet for at vælgge tid
@SuppressLint("DefaultLocale")
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String) ->Unit
)  {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF78B17E),
        contentColor = Color.White,
        disabledContainerColor = Color(0xFFE8F5E9),
        disabledContentColor = Color(0xFFB0BEC5)
    )

    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    AlertDialog(onDismissRequest = { onDismiss()},
        title = {Text("Select Time")},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically
                ){
                    Column(horizontalAlignment =Alignment.CenterHorizontally){
                      Text("Hours")
                       AndroidView(
                            factory = {context ->
                                NumberPicker(context).apply{
                                    minValue = 0
                                    maxValue = 23
                                    value = selectedHour
                                    setOnValueChangedListener { _, _, newVal ->
                                        selectedHour = newVal  }
                                }
                            },
                            modifier = Modifier.size(100.dp, 150.dp)  )
                    }


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Minutes")
                        AndroidView(
                            factory = { context ->
                                NumberPicker(context).apply {
                                    minValue = 0
                                    maxValue = 59
                                    value = selectedMinute
                                    setOnValueChangedListener { _, _, newVal ->
                                        selectedMinute = newVal
                                    }
                                }

                            },
                            modifier = Modifier.size(100.dp, 150.dp) )  }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    onTimeSelected(formattedTime)
                    onDismiss()
                },
                colors = buttonColors
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = buttonColors
            ) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ShowDifficultyDialog(
    onDismiss:() -> Unit,
    onDifficultySelected:(String) -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF78B17E),
        contentColor = Color.White,
        disabledContainerColor = Color(0xFFE8F5E9),
        disabledContentColor = Color(0xFFB0BEC5)
    )

    AlertDialog(
        onDismissRequest = { onDismiss()},
        title = { Text("Select Difficulty") },
        text = { Text("Please choose a difficulty level for your recipe.") },
        confirmButton = {
            Column {
                Button(onClick = {
                    onDifficultySelected("Easy")
                    onDismiss()
                },
                    colors = buttonColors
                ) {
                    Text("Easy")
                }
                Button(onClick = {
                    onDifficultySelected("Medium")
                    onDismiss()
                },
                    colors = buttonColors
                ) {
                    Text("Medium")
                }
                Button(onClick = {
                    onDifficultySelected("Hard")
                    onDismiss()
                },
                    colors = buttonColors
                ) {
                    Text("Hard")
                }
            }
        },
        dismissButton = {}
    )


}



