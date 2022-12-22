package gulshan.taksande.todo

import androidx.lifecycle.LiveData
import com.example.todoapp.data.TodoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(private val database: TodoDatabase) {


    var todoList: LiveData<List<TodoDetail>> = database.todoDao.getAllTodos()

    suspend fun insertTodo(todoDetail: TodoDetail) {
        withContext(Dispatchers.IO) {
            database.todoDao.insertTodo(todoDetail)
        }
    }

    suspend fun getAllTodo() {
        withContext(Dispatchers.IO) {
            todoList = database.todoDao.getAllTodos()
        }
    }

    suspend fun deleteTodo(todoDetail: TodoDetail) {
        database.todoDao.deleteTodo(todoDetail)
    }

    suspend fun updateTodo(todoDetail: TodoDetail) {
        database.todoDao.updateTodo(todoDetail)
    }

}