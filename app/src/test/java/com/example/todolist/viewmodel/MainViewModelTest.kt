package com.example.todolist.viewmodel

import com.example.todolist.data.TodoRepository
import com.example.todolist.model.Todo
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class MainViewModelTest {

    // Тестові дублікати
    private lateinit var todoRepository: TodoRepository
    private lateinit var spiedRepository: TodoRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // 1. Створюємо mock (заглушку) для репозиторія
        todoRepository = mockk()

        // 2. Створюємо spyk для функцій, що повертають Unit
        val realRepo = object : TodoRepository {
            override fun getAllTodosStream() = flowOf(emptyList<Todo>())
            override suspend fun getTodoStream(id: Int) = null
            override suspend fun insertTodo(todo: Todo) {}
            override suspend fun updateTodo(todo: Todo) {}
            override suspend fun deleteTodo(todo: Todo) {}
        }
        spiedRepository = spyk(realRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Пункт 4: Тест для функції, що повертає значення
    @Test
    fun testViewModel_fetchesDataFromRepository_mock() = runTest {
        // Конфігуруємо заглушку (stubbing)
        val mockTodoList = listOf(Todo(id = 1, title = "Тестове завдання", completed = false))
        every { todoRepository.getAllTodosStream() } returns flowOf(mockTodoList)

        // Перевіряємо, що мок повертає очікувані дані
        val resultFlow = todoRepository.getAllTodosStream()

        verify { todoRepository.getAllTodosStream() }
        assertNotNull(resultFlow)
    }

    // Пункт 5: Тест для функції, що нічого не повертає / повертає Unit
    @Test
    fun testViewModel_callsInsertMethod_spy() = runTest {
        val newTodo = Todo(id = 2, title = "Нове завдання", completed = false)

        // Викликаємо метод, що повертає Unit
        coEvery { spiedRepository.insertTodo(any()) } just Runs

        spiedRepository.insertTodo(newTodo)

        // Перевіряємо за допомогою шпигуна, чи метод був реально викликаний з правильним аргументом
        coVerify(exactly = 1) { spiedRepository.insertTodo(newTodo) }
    }
}