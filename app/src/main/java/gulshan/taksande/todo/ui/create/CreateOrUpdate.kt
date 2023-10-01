import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import gulshan.taksande.todo.TodoViewModel
import gulshan.taksande.todo.data.TodoDetail
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTodo(navController: NavHostController, viewModel: TodoViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var dateTime by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var selectedTime by rememberSaveable { mutableStateOf(LocalTime.now()) }
    val selectedItem = viewModel.selectedItem
    if (selectedItem != null) {
        text = selectedItem.title
        description = selectedItem.description
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column {
            OutlinedTextField(value = text, onValueChange = {
                text = it
            }, label = { Text("Title") })
            OutlinedTextField(value = description, onValueChange = {
                description = it
            }, label = { Text("Description") })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), onClick = {
                    val todoItem =
                        TodoDetail(title = text, description = description, isCompleted = false)
                    if (viewModel.selectedItem == null) viewModel.insertTodo(todoItem)
                    else {
                        todoItem.id = selectedItem!!.id
                        viewModel.updateTodo(todoItem)
                    }
                    viewModel.selectedItem = null
                    navController.navigateUp()
                }, content = { Text("Submit") })
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), onClick = {
                    viewModel.selectedItem = null
                    navController.navigateUp()
                }, content = { Text("Cancel") })

            }
        }
        BackHandler(
            enabled = true
        ) {
            viewModel.selectedItem = null
        }
    }
}

@Composable
fun createDatePicker() {


}
