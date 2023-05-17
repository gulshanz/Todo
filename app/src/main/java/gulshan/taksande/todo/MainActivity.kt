package gulshan.taksande.todo

import CreateTodo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import gulshan.taksande.todo.ui.list.CustomRv
import gulshan.taksande.todo.ui.theme.TodoTheme


class MainActivity : ComponentActivity() {
    lateinit var repository: TodoRepository
    lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, true)
        val database = getDatabase(applicationContext)
        repository = TodoRepository(database)
        val factory = TodoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]

        setContent {
            TodoTheme {
                /* A surface container using the 'background' color from the theme */
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "TopAppBar")
                        },
                        )
                },
                    content = {
                        TodoApp(viewModel)
                    }

                )
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