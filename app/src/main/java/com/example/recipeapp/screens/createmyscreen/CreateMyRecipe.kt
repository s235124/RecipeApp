import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.screens.createmyscreen.CreateMyRecipeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Row

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateMyRecipe(onSaveClick: () -> Unit) {
    val context = LocalContext.current
    val viewModel: CreateMyRecipeViewModel =
        viewModel(factory = CreateMyRecipeViewModelFactory(context))

    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("Select Time") }
    var difficulty by remember { mutableStateOf("Select Difficulty") }
    var calories by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    val savedUri by viewModel.imageUriFlow.collectAsState()

    // Restore saved image URI from ViewModel or DataStore
    LaunchedEffect(savedUri) {
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri)
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            viewModel.saveImageUri(uri.toString()) // Save image URI to DataStore
        }
    }

    //Logik for knappene på skærmen  (tids- og sværhedsvælger)


    if (showTimePicker) {
        TimePickerDialo(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { selectedTime ->
                time = selectedTime }
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

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Selection
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            } else {
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Select Image")
                }
            }

            // Input Fields
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Recipe Name") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(time) //Viser valgt tid or standardtekst
        }

        //Sværhedsgrad knap
        Button(
            onClick = { showDifficultyDialog = true },
            modifier = Modifier.fillMaxWidth()
        ){
            Text(difficulty)
        }

        TextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories") },
            modifier = Modifier.fillMaxWidth()
        )

            // Description
            MultiLineTextField(
                text = description,
                placeholder = "Write a description for your recipe...",
                onTextChange = { description = it }
            )

            // Save Button
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
                            ingredient = ingredients
                        )
                    )
                    onSaveClick()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Recipe")
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
            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
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
@Composable
fun TimePickerDialo(
    onDismiss: () -> Unit,
    onTimeSelected: (String) ->Unit
)  {
    var selectedHour by remember { mutableStateOf(0) }
    var selectedMinute by remember { mutableStateOf(0) }

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
        confirmButton = { Button(onClick = {
                val formattedTime = String.format("%02d:%02d",selectedHour, selectedMinute)
                onTimeSelected(formattedTime)
                onDismiss()  }) { Text("OK") } },

        dismissButton = { Button(onClick = { onDismiss() }) {
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
    AlertDialog(
        onDismissRequest = { onDismiss()},
        title = { Text("Select Difficulty") },
        text = { Text("Please choose a difficulty level for your recipe.") },
        confirmButton = {
            Column {
                Button(onClick = {
                    onDifficultySelected("Easy")
                    onDismiss()
                }) {
                    Text("Easy")
                }
                Button(onClick = {
                    onDifficultySelected("Medium")
                    onDismiss()
                }) {
                    Text("Medium")
                }
                Button(onClick = {
                    onDifficultySelected("Hard")
                    onDismiss()
                }) {
                    Text("Hard")
                }
            }
        },
        dismissButton = {}
    )


}



