package com.example.todolist.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolist.TodoApplication
import com.example.todolist.viewmodel.TodoViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TodoViewModel(todoApplication().container.todoRepository)
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)