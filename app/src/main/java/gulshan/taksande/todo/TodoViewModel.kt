package gulshan.taksande.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import gulshan.taksande.todo.data.TodoDetail
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val title: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val isCompleted: MutableLiveData<Boolean> = MutableLiveData()

     var allTodos = repository.getAllTodo()


    fun deleteTodo(todo: TodoDetail) {
        viewModelScope.launch {
            repository.deleteTodo(todoDetail = todo)
        }
    }

    fun editTodo(todo: TodoDetail) {
        viewModelScope.launch {
            repository.updateTodo(todoDetail = todo)
        }
    }

    fun insertTodo(todo: TodoDetail) {
        viewModelScope.launch {
            repository.insertTodo(todo)
        }
    }
}


class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(repository) as T
    }
}
