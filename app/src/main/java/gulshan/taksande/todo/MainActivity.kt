package gulshan.taksande.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import gulshan.taksande.todo.data.TodoDetail
import gulshan.taksande.todo.ui.theme.TodoTheme


class MainActivity : ComponentActivity() {
    lateinit var repository: TodoRepository
    lateinit var viewModel: TodoViewModel
    var list = emptyList<TodoDetail>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = getDatabase(applicationContext)
        repository = TodoRepository(database)
        val factory = TodoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]

        setContent {
            TodoTheme {
                /* A surface container using the 'background' color from the theme */
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    TodoApp(viewModel)
                }
            }
        }

    }
}


@Composable
fun TodoApp(viewModel: TodoViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.TodoList.route) {
        composable(route = Screen.TodoList.route) {
            CustomRv(navController, viewModel)
        }
        composable(route = Screen.TodoCreateOrEdit.route) {
            CreateTodo(navController, viewModel)
        }
    }
}


@Composable
fun CustomRv(navController: NavHostController, viewModel: TodoViewModel) {
    // flow

    val listFlow = viewModel.allTodos.collectAsState(initial = emptyList())
    val list = listFlow.value
    Scaffold(
        floatingActionButton = { FloatingActionButton(onClick = {
            navController.navigate(Screen.TodoCreateOrEdit.route)
            viewModel.selectedTodo = null
        }) {} },
        content = {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(list.size, itemContent = { item ->
                    TodoCard(emp = list[item]) {
                        viewModel.selectedTodo = list[item]
                        navController.navigate(Screen.TodoCreateOrEdit.route)
                    }
                })
            }
        }
    )
}

@Preview
@Composable
fun TodoCard(emp: TodoDetail = TodoDetail(1, "efce", "fwe", false), onClick: () -> Unit = {}) {
    val checkState = remember { mutableStateOf(emp.isCompleted) }
    Surface(modifier = Modifier
        .clickable {
            onClick.invoke()
        }
        .padding(
            horizontal = 8.dp, vertical = 8.dp
        )
        .fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Column(modifier = Modifier.weight(1f), Arrangement.Center) {
                Text(
                    text = emp.title, style = TextStyle(
                        color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Description -:" + emp.description, style = TextStyle(
                        color = Color.Black
                    )
                )

            }
            Checkbox(checked = checkState.value, onCheckedChange = {
                checkState.value = it
            })
        }
    }
}


@Composable
fun CreateTodo(navController: NavHostController, viewModel: TodoViewModel) {
    var text by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        viewModel.selectedTodo?.let {
            text = it.title
            description = it.description
        }

        Column {
            OutlinedTextField(value = text, onValueChange = {
                text = it
            }, label = { Text("Title") })
            OutlinedTextField(value = description, onValueChange = {
                description = it
            }, label = { Text("Description") })

            Button(onClick = {
                val todoItem =
                    TodoDetail(title = text, description = description, isCompleted = false)
                viewModel.insertTodo(todoItem)
                navController.navigateUp()
            }, content = { Text("Submit") })
        }
    }
}


fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
        this.route = route
        arguments.forEach { (argumentName, argument) ->
            addArgument(argumentName, argument)
        }
        deepLinks.forEach { deepLink ->
            addDeepLink(deepLink)
        }
    })
}