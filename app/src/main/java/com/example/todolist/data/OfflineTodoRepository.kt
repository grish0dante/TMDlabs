package com.example.todolist.data

import com.example.todolist.model.Todo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class OfflineTodoRepository(private val todoDao: TodoDao) : TodoRepository {
    override fun getAllTodosStream(): Flow<List<Todo>> = todoDao.getAllTodos()

    override suspend fun getTodoStream(id: Int): Todo? {
        if (id <= 0) {
            throw IllegalArgumentException("ID завдання не може бути меншим або рівним 0 (передано: $id)")
        }
        return todoDao.getTodo(id)
    }

    override suspend fun insertTodo(todo: Todo) {
        delay(1200) // Штучна затримка виконання (1.2 секунди)
        todoDao.insert(todo)
    }

    override suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo)
    override suspend fun updateTodo(todo: Todo) = todoDao.update(todo)
}
