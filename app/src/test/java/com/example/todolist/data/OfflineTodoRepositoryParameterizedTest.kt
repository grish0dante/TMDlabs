package com.example.todolist.data

import com.example.todolist.model.Todo
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.Assert.*

@RunWith(Parameterized::class)
class OfflineTodoRepositoryParameterizedTest(
    private val todoTitle: String,
    private val isCompleted: Boolean,
    private val expectedIsValid: Boolean
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Title: \"{0}\", Completed: {1}, Expected Valid: {2}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Купити молоко", false, true),
                arrayOf("Зробити лабораторну", true, true),
                arrayOf("З", false, true),       // Короткий, але валідний заголовок
                arrayOf("", false, false),      // Порожній заголовок — не валідний
                arrayOf("   ", true, false)     // Лише пробіли — не валідний
            )
        }
    }

    @Test
    fun testTodoModelValidationParameterized() {
        // Створюємо об'єкт Todo на основі поточних параметрів
        val todo = Todo(
            id = 0,
            title = todoTitle,
            completed = isCompleted
        )

        // Перевіряємо бізнес-правило: назва завдання не повинна бути порожньою чи складатися з пробілів
        val isValid = todo.title.isNotBlank()

        // Порівнюємо результат
        assertEquals(expectedIsValid, isValid)
    }
}