package com.example.todolist.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.todolist.data.TodoRepository
import com.example.todolist.model.Todo
import com.example.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Інструментальний параметризований UI-тест для перевірки внесення різних назв завдань на [EditScreen]
 */
@RunWith(Parameterized::class)
class EditScreenTitleParameterizedTest(
    private val inputTitle: String,
    private val expectedSavedTitle: String
) {
    @get:Rule
    val composeTestRule = createComposeRule()

    private class FakeRepository : TodoRepository {
        var savedTodo: Todo? = null
        override fun getAllTodosStream(): Flow<List<Todo>> = MutableStateFlow(emptyList())
        override suspend fun getTodoStream(id: Int): Todo? = null
        override suspend fun insertTodo(todo: Todo) { savedTodo = todo }
        override suspend fun deleteTodo(todo: Todo) {}
        override suspend fun updateTodo(todo: Todo) {}
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Вхідна назва: \"{0}\" -> Збережена назва: \"{1}\"")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("Купити молоко", "Купити молоко"),
                arrayOf("Зробити лабораторну роботу", "Зробити лабораторну роботу"),
                arrayOf("Тестова задача 123 !@#", "Тестова задача 123 !@#")
            )
        }
    }

    @Test
    fun testEditScreen_titleInputAndSave() {
        val fakeRepo = FakeRepository()
        val viewModel = TodoViewModel(fakeRepo)

        composeTestRule.setContent {
            val navController = rememberNavController()
            EditScreen(
                taskToEdit = null,
                viewModel = viewModel,
                navController = navController
            )
        }

        // Введення параметризованої назви в поле TextField
        composeTestRule.onNodeWithText("Назва задачі").performTextInput(inputTitle)

        // Натискання кнопки збереження
        composeTestRule.onNodeWithText("💾 Зберегти").performClick()

        // Перевірка, що збережене завдання отримало правильну назву
        assertEquals(expectedSavedTitle, fakeRepo.savedTodo?.title)
    }
}
