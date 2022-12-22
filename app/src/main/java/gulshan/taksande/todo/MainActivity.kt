package gulshan.taksande.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import gulshan.taksande.todo.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {
    lateinit var repository: TodoRepository
    lateinit var viewModel: TodoViewModel

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
    val employees: List<TodoDetail> = try {
        viewModel.allTodos.value!!
    } catch (e: Exception) {
        emptyList()
    }

    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(employees.size, itemContent = { item ->
            TodoCard(emp = employees[item]) {
                navController.navigate(Screen.TodoCreateOrEdit.route)
            }
        })
    }
    FloatingActionButton(onClick = { navController.navigate(Screen.TodoCreateOrEdit.route) }) {}

}


@Composable
fun TodoCard(emp: TodoDetail, onClick: () -> Unit) {
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