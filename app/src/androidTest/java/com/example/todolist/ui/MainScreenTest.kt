package com.example.todolist.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todolist.model.Todo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Інструментальні UI-тести для закладинки / екрана [MainScreen] (Головний екран)
 */
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreen_displaysHeaderAndTasksList() {
        val tasks = listOf(
            Todo(id = 1, title = "Перше завдання", completed = false),
            Todo(id = 2, title = "Друге завдання", completed = true)
        )

        composeTestRule.setContent {
            MainScreen(
                tasks = tasks,
                onAddClick = {},
                onEditClick = {},
                onDeleteClick = {},
                onStatusChange = { _, _ -> }
            )
        }

        // Перевірка відображення заголовка екрана та завдань
        composeTestRule.onNodeWithText("ToDo List").assertIsDisplayed()
        composeTestRule.onNodeWithText("Перше завдання").assertIsDisplayed()
        composeTestRule.onNodeWithText("Друге завдання").assertIsDisplayed()
    }

    @Test
    fun mainScreen_clickAddButton_triggersCallback() {
        var addClicked = false

        composeTestRule.setContent {
            MainScreen(
                tasks = emptyList(),
                onAddClick = { addClicked = true },
                onEditClick = {},
                onDeleteClick = {},
                onStatusChange = { _, _ -> }
            )
        }

        // Натискання кнопки додавання завдання
        composeTestRule.onNodeWithText("➕ Додати задачу").performClick()
        assertTrue(addClicked)
    }

    @Test
    fun mainScreen_clickDeleteButton_triggersDeleteCallback() {
        var deletedTask: Todo? = null
        val task = Todo(id = 1, title = "Завдання для видалення", completed = false)

        composeTestRule.setContent {
            MainScreen(
                tasks = listOf(task),
                onAddClick = {},
                onEditClick = {},
                onDeleteClick = { deletedTask = it },
                onStatusChange = { _, _ -> }
            )
        }

        // Натискання на іконку видалення
        composeTestRule.onAllNodesWithContentDescription("Видалити")[0].performClick()
        assertEquals(task, deletedTask)
    }

    @Test
    fun mainScreen_clickTaskItem_triggersEditCallback() {
        var editedTask: Todo? = null
        val task = Todo(id = 1, title = "Завдання для редагування", completed = false)

        composeTestRule.setContent {
            MainScreen(
                tasks = listOf(task),
                onAddClick = {},
                onEditClick = { editedTask = it },
                onDeleteClick = {},
                onStatusChange = { _, _ -> }
            )
        }

        // Клацання на завдання відкриває екран редагування
        composeTestRule.onNodeWithText("Завдання для редагування").performClick()
        assertEquals(task, editedTask)
    }
}
