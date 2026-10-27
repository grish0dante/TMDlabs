package com.example.todolist.data

import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class OfflineTodoRepositoryNegativeTest {

    private lateinit var repository: OfflineTodoRepository

    @Before
    fun setUp() {
        val fakeDao = object : TodoDao {
            // Виправляємо помилку, явно вказуючи тип List<Todo>
            override fun getAllTodos() = flowOf<List<Todo>>(emptyList())

            override suspend fun getTodo(id: Int) = null
            override suspend fun insert(todo: Todo) {}
            override suspend fun update(todo: Todo) {}
            override suspend fun delete(todo: Todo) {}
        }
        repository = OfflineTodoRepository(fakeDao)
    }

    // Пункт 9.1: Тест очікує генерування виключення (IllegalArgumentException) при некоректному ID (<= 0)
    @Test(expected = IllegalArgumentException::class)
    fun testGetTodoStream_throwsException_whenIdIsInvalid() {
        runBlocking {
            repository.getTodoStream(0) // Має викинути виняток
        }
    }

    // Пункт 9.2: Тест на перевірку виконання за час, що перевищує таймаут
    @Test
    fun testInsertTodoExecutionTimeExceedsTimeout() {
        val startTime = System.currentTimeMillis()

        runBlocking {
            // Викликаємо метод із затримкою з репозиторію
            repository.insertTodo(Todo(id = 2, title = "Нове завдання", completed = false))
        }

        val duration = System.currentTimeMillis() - startTime
        assertTrue("Час виконання ($duration мс) має перевищувати 1000 мс", duration >= 1000)
    }
}