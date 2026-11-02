package com.example.todolist.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.todolist.model.Todo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Інструментальний параметризований UI-тест для перевірки режимів фільтрації на [MainScreen]
 */
@RunWith(Parameterized::class)
class MainScreenFilterParameterizedTest(
    private val filterChipLabel: String,
    private val expectedVisibleTaskTitle: String,
    private val expectedHiddenTaskTitle: String?
) {
    @get:Rule
    val composeTestRule = createComposeRule()

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "Фільтр: \"{0}\", Очікувана видима задача: \"{1}\"")
        fun data(): Collection<Array<Any?>> {
            return listOf(
                arrayOf("Всі", "Активна задача", "Виконана задача"),
                arrayOf("Активні", "Активна задача", "Виконана задача"),
                arrayOf("Виконані", "Виконана задача", "Активна задача")
            )
        }
    }

    @Test
    fun testFilterChips_displaysCorrectTasks() {
        val tasks = listOf(
            Todo(id = 1, title = "Активна задача", completed = false),
            Todo(id = 2, title = "Виконана задача", completed = true)
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

        // Клік на чип фільтра
        composeTestRule.onNodeWithText(filterChipLabel).performClick()

        // Перевірка наявності очікуваного елемента
        composeTestRule.onNodeWithText(expectedVisibleTaskTitle).assertIsDisplayed()

        // Перевірка відсутності прихованого елемента для фільтрів "Активні" та "Виконані"
        if (filterChipLabel != "Всі" && expectedHiddenTaskTitle != null) {
            composeTestRule.onNodeWithText(expectedHiddenTaskTitle).assertDoesNotExist()
        }
    }
}
