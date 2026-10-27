package com.example.todolist.data

import com.example.todolist.model.Todo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class OfflineTodoRepositoryTest {

    private lateinit var fakeDao: TodoDao
    private lateinit var repository: OfflineTodoRepository

    // Фікстура: виконується перед кожним тестом для налаштування середовища
    @Before
    fun setUp() {
        fakeDao = object : TodoDao {
            private val list = mutableListOf(Todo(id = 1, title = "Завдання 1", completed = false))

            override fun getAllTodos() = flowOf(list)

            override suspend fun getTodo(id: Int) = list.find { it.id == id }

            override suspend fun insert(todo: Todo) {
                list.add(todo)
            }

            override suspend fun update(todo: Todo) {
                val index = list.indexOfFirst { it.id == todo.id }
                if (index != -1) list[index] = todo
            }

            override suspend fun delete(todo: Todo) {
                list.remove(todo)
            }
        }
        repository = OfflineTodoRepository(fakeDao)
    }

    @Test
    fun testGetAllTodosStream_positive() = runBlocking {
        val result = repository.getAllTodosStream().first()
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Завдання 1", result[0].title)
    }

    @Test
    fun testGetTodoStream_positive() = runBlocking {
        val todo = repository.getTodoStream(1)
        assertNotNull(todo)
        assertEquals(1, todo?.id)
        assertEquals("Завдання 1", todo?.title)
    }
}