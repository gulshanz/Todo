package gulshan.taksande.todo
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import gulshan.taksande.todo.data.TodoDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoDetail")
    fun getAllTodos(): Flow<MutableList<TodoDetail>>

    @Update
    fun updateTodo(todoObj: TodoDetail)

    @Delete
    fun deleteTodo(todoObj: TodoDetail)

    @Insert
    fun insertTodo(todoObj: TodoDetail)

    @Query("DELETE FROM TodoDetail")
    fun deleteAll()
}

@Database(entities = [TodoDetail::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao
}

private lateinit var INSTANCE: TodoDatabase

fun getDatabase(context: Context): TodoDatabase {
    synchronized(TodoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todoDb"
            ).build()
        }
        return INSTANCE
    }
}