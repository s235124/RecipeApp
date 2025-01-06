import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CreateMyRecipe() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Close Button (Top Left Corner)
        IconButton(onClick = { /* Handle close */ }, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), // Use your custom close icon
                contentDescription = "Close",
                tint = Color.Black
            )
        }

        // Upload Picture Button
        Button(
            onClick = { /* Handle upload picture */ },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Upload picture", fontSize = 16.sp)
        }

        EditableField("Recipe name", "Enter recipe name here...")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AddFieldButton("Time")
            AddFieldButton("Difficulty")
            AddFieldButton("Calories")
        }

        Spacer(modifier = Modifier.height(16.dp))

        EditableField("Description", "Describe your recipe and what you can expect from it...")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingredients",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        repeat(3) {
            AddButton()
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        EditableField("Method", "Make a step by step method on how to make this recipe...")
    }
}

@Composable
fun EditableField(title: String, hint: String) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = hint) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun AddFieldButton(text: String) {
    OutlinedButton(
        onClick = { /* Handle button click */ },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
    ) {
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun AddButton() {
    OutlinedButton(
        onClick = { /* Handle add ingredient */ },
        shape = CircleShape,
        modifier = Modifier.size(48.dp)
    ) {
        Text("+", textAlign = TextAlign.Center, fontSize = 18.sp)
    }
}
