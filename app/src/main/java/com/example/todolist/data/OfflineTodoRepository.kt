package com.example.todolist.data

import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

class OfflineTodoRepository(private val todoDao: TodoDao) : TodoRepository {
    override fun getAllTodosStream(): Flow<List<Todo>> = todoDao.getAllTodos()

    override suspend fun getTodoStream(id: Int): Todo? = todoDao.getTodo(id)

    override suspend fun insertTodo(todo: Todo) = todoDao.insert(todo)

    override suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo)

    override suspend fun updateTodo(todo: Todo) = todoDao.update(todo)
}