package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.model.Todo

@Composable
fun MainScreen(
    tasks: List<Todo>,
    onAddClick: () -> Unit,
    onEditClick: (Todo) -> Unit,
    onDeleteClick: (Todo) -> Unit,
    onStatusChange: (Todo, Boolean) -> Unit
) {
    var filterStatus by rememberSaveable { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("ToDo List", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            Text("Фільтр:", modifier = Modifier.padding(end = 8.dp))
            FilterChip(selected = filterStatus == "All", onClick = { filterStatus = "All" }, label = { Text("Всі") })
            Spacer(Modifier.width(4.dp))
            FilterChip(selected = filterStatus == "Active", onClick = { filterStatus = "Active" }, label = { Text("Активні") })
            Spacer(Modifier.width(4.dp))
            FilterChip(selected = filterStatus == "Completed", onClick = { filterStatus = "Completed" }, label = { Text("Виконані") })
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddClick) { Text("➕ Додати задачу") }
        Spacer(Modifier.height(16.dp))

        val filteredTasks = tasks.filter { task ->
            when (filterStatus) {
                "Active" -> !task.completed
                "Completed" -> task.completed
                else -> true
            }
        }

        LazyColumn {
            items(filteredTasks, key = { it.id }) { todo ->
                TodoItem(
                    todo = todo,
                    onEditClick = { onEditClick(todo) },
                    onDeleteClick = { onDeleteClick(todo) },
                    onStatusChange = { isChecked -> onStatusChange(todo, isChecked) }
                )
            }
        }
    }
}