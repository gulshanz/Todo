package gulshan.taksande.todo.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gulshan.taksande.todo.TodoViewModel
import gulshan.taksande.todo.data.TodoDetail

@Composable
fun TodoCard(
    emp: TodoDetail = TodoDetail(1, "efce", "fwe", false),
    viewModel: TodoViewModel,
    onClick: () -> Unit = {}
) {
    val checkState = remember { mutableStateOf(emp.isCompleted) }
    Card(modifier = Modifier
        .clickable {
            onClick.invoke()
        }
        .padding(
            horizontal = 2.dp, vertical = 2.dp
        )
        .fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            Column(modifier = Modifier.weight(1f), Arrangement.SpaceBetween) {
                Text(
                    text = emp.title, style = TextStyle(
                        color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "Description -:" + emp.description, style = TextStyle(
                        color = Color.Black
                    )
                )

            }
            Checkbox(checked = checkState.value, onCheckedChange = {
                emp.isCompleted = !emp.isCompleted
                viewModel.editTodo(emp)
                checkState.value = it
            })
        }
    }
}