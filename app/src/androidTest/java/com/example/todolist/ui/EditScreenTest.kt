package com.example.todolist.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todolist.data.TodoRepository
import com.example.todolist.model.Todo
import com.example.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Інструментальні UI-тести для екрана створення/редагування [EditScreen]
 */
@RunWith(AndroidJUnit4::class)
class EditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private class FakeRepository : TodoRepository {
        var insertedTodo: Todo? = null
        var updatedTodo: Todo? = null

        override fun getAllTodosStream(): Flow<List<Todo>> = MutableStateFlow(emptyList())
        override suspend fun getTodoStream(id: Int): Todo? = null
        override suspend fun insertTodo(todo: Todo) { insertedTodo = todo }
        override suspend fun deleteTodo(todo: Todo) {}
        override suspend fun updateTodo(todo: Todo) { updatedTodo = todo }
    }

    @Test
    fun editScreen_createNewTask_displaysFormFields() {
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

        // Перевірка наявності основних елементів інтерфейсу створення
        composeTestRule.onNodeWithText("Створити задачу").assertIsDisplayed()
        composeTestRule.onNodeWithText("Назва задачі").assertIsDisplayed()
        composeTestRule.onNodeWithText("Виберіть дату").assertIsDisplayed()
        composeTestRule.onNodeWithText("Початок").assertIsDisplayed()
        composeTestRule.onNodeWithText("Кінець").assertIsDisplayed()
        composeTestRule.onNodeWithText("Скасувати").assertIsDisplayed()
        composeTestRule.onNodeWithText("💾 Зберегти").assertIsDisplayed()
    }

    @Test
    fun editScreen_editExistingTask_prefillsTaskData() {
        val fakeRepo = FakeRepository()
        val viewModel = TodoViewModel(fakeRepo)
        val existingTask = Todo(id = 5, title = "Існуюче завдання", completed = true)

        composeTestRule.setContent {
            val navController = rememberNavController()
            EditScreen(
                taskToEdit = existingTask,
                viewModel = viewModel,
                navController = navController
            )
        }

        // Перевірка попереднього заповнення
        composeTestRule.onNodeWithText("Редагувати").assertIsDisplayed()
        composeTestRule.onNodeWithText("Існуюче завдання").assertIsDisplayed()
    }

    @Test
    fun editScreen_clickDateButton_opensDatePickerDialog() {
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

        // Відкриття діалогу вибору дати
        composeTestRule.onNodeWithText("Виберіть дату").performClick()
        composeTestRule.onNodeWithText("Оберіть дату").assertIsDisplayed()
    }

    @Test
    fun editScreen_clickStartTimeButton_opensTimePickerDialog() {
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

        // Відкриття діалогу вибору часу початку
        composeTestRule.onNodeWithText("Початок").performClick()
        composeTestRule.onNodeWithText("Час початку").assertIsDisplayed()
    }

    @Test
    fun editScreen_clickEndTimeButton_opensTimePickerDialog() {
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

        // Відкриття діалогу вибору часу завершення
        composeTestRule.onNodeWithText("Кінець").performClick()
        composeTestRule.onNodeWithText("Час завершення").assertIsDisplayed()
    }
}
