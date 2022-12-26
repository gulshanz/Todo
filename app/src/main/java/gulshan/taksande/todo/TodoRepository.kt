package gulshan.taksande.todo

import androidx.lifecycle.LiveData
import gulshan.taksande.todo.data.TodoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoRepository(private val database: TodoDatabase) {


    var todoList = database.todoDao.getAllTodos()


    suspend fun insertTodo(todoDetail: TodoDetail) {
        withContext(Dispatchers.IO) {
            database.todoDao.insertTodo(todoDetail)
        }
    }

    fun getAllTodo(): LiveData<List<TodoDetail>> {
        return database.todoDao.getAllTodos()
    }

    suspend fun deleteTodo(todoDetail: TodoDetail) {
        database.todoDao.deleteTodo(todoDetail)
    }

    suspend fun updateTodo(todoDetail: TodoDetail) {
        database.todoDao.updateTodo(todoDetail)
    }

}