package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoRepository
import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {

    val tasks: StateFlow<List<Todo>> = todoRepository.getAllTodosStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, date: Date?, start: Date?, end: Date?, isCompleted: Boolean) {
        viewModelScope.launch {
            todoRepository.insertTodo(
                Todo(title = title, plannedDate = date, plannedStartTime = start, plannedEndTime = end, completed = isCompleted)
            )
        }
    }

    fun updateTask(id: Int, title: String, date: Date?, start: Date?, end: Date?, isCompleted: Boolean) {
        viewModelScope.launch {
            todoRepository.updateTodo(
                Todo(id = id, title = title, plannedDate = date, plannedStartTime = start, plannedEndTime = end, completed = isCompleted)
            )
        }
    }

    fun toggleTaskCompletion(todo: Todo, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(completed = isCompleted)
            println("DEBUG_TODO: Trying to update item with ID: ${updatedTodo.id}, New Status: $isCompleted")

            todoRepository.updateTodo(updatedTodo)
        }
    }

    fun removeTask(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}