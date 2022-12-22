package gulshan.taksande.todo

sealed class Screen(val route:String){
    object TodoList: Screen("todoList")
    object TodoCreateOrEdit: Screen("todoCreateOrEditList")
}
