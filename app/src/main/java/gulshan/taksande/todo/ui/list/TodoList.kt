package gulshan.taksande.todo.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import gulshan.taksande.todo.Screen
import gulshan.taksande.todo.TodoViewModel

@Composable
fun CustomRv(navController: NavHostController, viewModel: TodoViewModel) {
    val listState by viewModel.allTodos.collectAsState(initial = emptyList())
    val list = listState
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate(Screen.TodoCreateOrEdit.route) }) {
            Icon(Icons.Filled.Add, "")
        }
    }, content = {
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            items(list.size, itemContent = { item ->
                TodoCard(emp = list[item], viewModel) {
                    viewModel.selectedItem(list[item])
                    navController.navigate(Screen.TodoCreateOrEdit.route)
                }
            })
        }
    })
}