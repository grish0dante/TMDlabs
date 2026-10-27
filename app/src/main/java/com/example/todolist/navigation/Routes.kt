package com.example.todolist.navigation

import com.example.todolist.model.Todo
import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Main : Screen

    // Якщо null - створюємо нову, якщо об'єкт - редагуємо.
    @Serializable
    data class Edit(val task: Todo?) : Screen
}