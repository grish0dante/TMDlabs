package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.todolist.model.Todo
import com.example.todolist.ui.AppViewModelProvider
import com.example.todolist.ui.EditScreen
import com.example.todolist.ui.MainScreen
import com.example.todolist.viewmodel.TodoViewModel
import kotlin.reflect.typeOf

@Composable
fun NavGraph(navController: NavHostController) {

    // фабрика для створення ViewModel з репозиторієм
    val todoViewModel: TodoViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(navController = navController, startDestination = Screen.Main) {

        composable<Screen.Main> {
            val tasks by todoViewModel.tasks.collectAsState()

            MainScreen(
                tasks = tasks,
                onAddClick = { navController.navigate(Screen.Edit(null)) },
                onEditClick = { task -> navController.navigate(Screen.Edit(task)) },
                onDeleteClick = { task -> todoViewModel.removeTask(task) },
                onStatusChange = { task, checked -> todoViewModel.toggleTaskCompletion(task, checked) }
            )
        }

        composable<Screen.Edit>(
            typeMap = mapOf(typeOf<Todo?>() to TodoNavType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Edit>()
            EditScreen(
                taskToEdit = args.task,
                viewModel = todoViewModel,
                navController = navController
            )
        }
    }
}