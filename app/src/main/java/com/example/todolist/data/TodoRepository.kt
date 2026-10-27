package com.example.todolist.data

import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodosStream(): Flow<List<Todo>>
    suspend fun getTodoStream(id: Int): Todo?
    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
}