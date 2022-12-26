package gulshan.taksande.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoDetail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    var isCompleted: Boolean
)