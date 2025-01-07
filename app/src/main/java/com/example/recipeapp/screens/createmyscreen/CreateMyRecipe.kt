import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.model.Recipe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Composable
fun CreateMyRecipe(navController: NavController) {
    val context = LocalContext.current
    val viewModel: CreateMyRecipeViewModel= viewModel(factory = CreateMyRecipeViewModelFactory(context))

    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        TextField(value = name, onValueChange = { name = it }, label = { Text("Recipe Name") })
        TextField(value = time, onValueChange = { time = it }, label = { Text("Time") })
        TextField(value = difficulty, onValueChange = { difficulty = it }, label = { Text("Difficulty") })
        TextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories") })

        Button(
            onClick = {
                viewModel.saveRecipe(
                    Recipe(
                        name = name,
                        time = time,
                        difficulty = difficulty,
                        calories = calories,
                        imageUri = imageUri?.toString(),
                        categories = "Custom"
                    )
                )
                navController.popBackStack()
            }
        ) {
            Text("Save Recipe")
        }
    }
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
