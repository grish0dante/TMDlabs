package com.example.todolist.appium

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.net.URI
import java.time.Duration

/**
 * Функціональні UI-тести Appium для застосунку "ToDoList".
 *
 * Оптимізований тест 4 з підтвердженням переходу стану завдання між фільтрами "Активні" та "Виконані".
 */
class ToDoListAppiumTest {

    private lateinit var driver: AndroidDriver
    private lateinit var wait: WebDriverWait

    @BeforeEach
    fun setUp() {
        val apkPath = getApkAbsolutePath()

        val options = UiAutomator2Options().apply {
            setPlatformName("Android")
            setDeviceName("Medium Phone")
            setApp(apkPath)
            setAutomationName("UiAutomator2")
            setNoReset(false)
            setCapability("appium:enforceXPath1", true)
        }

        driver = AndroidDriver(URI("http://127.0.0.1:4723/").toURL(), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        wait = WebDriverWait(driver, Duration.ofSeconds(10))
    }

    private fun getApkAbsolutePath(): String {
        val userDir = File(System.getProperty("user.dir") ?: ".")
        val candidate1 = File(userDir, "app/build/outputs/apk/debug/app-debug.apk")
        if (candidate1.exists()) return candidate1.absolutePath
        val candidate2 = File(userDir.parentFile, "app/build/outputs/apk/debug/app-debug.apk")
        if (candidate2.exists()) return candidate2.absolutePath
        return "U:\\WorkDesk\\TMD\\ToDoList-master\\app\\build\\outputs\\apk\\debug\\app-debug.apk"
    }

    private fun isElementPresent(by: By): Boolean {
        driver.manage().timeouts().implicitlyWait(Duration.ZERO)
        val exists = driver.findElements(by).isNotEmpty()
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        return exists
    }

    private fun toggleEditScreenCheckbox(targetChecked: Boolean = true) {
        val completedCheckboxLocator = By.xpath("//*[@text='Виконано']/..//android.widget.CheckBox")
        val completedCheckbox = wait.until(ExpectedConditions.elementToBeClickable(completedCheckboxLocator))
        val targetStr = if (targetChecked) "true" else "false"

        if (completedCheckbox.getAttribute("checked") != targetStr) {
            completedCheckbox.click()
            try {
                WebDriverWait(driver, Duration.ofSeconds(2)).until {
                    driver.findElement(completedCheckboxLocator).getAttribute("checked") == targetStr
                }
            } catch (_: Exception) {
                driver.findElement(By.xpath("//*[@text='Виконано']")).click()
                WebDriverWait(driver, Duration.ofSeconds(2)).until {
                    driver.findElement(completedCheckboxLocator).getAttribute("checked") == targetStr
                }
            }
        }
    }

    @Test
    @DisplayName("1. Перевірка відображення заголовка та елементів головного екрана")
    fun testAppLaunchAndMainScreenElements() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='ToDo List']")))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Фільтр:']")))

        assertTrue(isElementPresent(By.xpath("//*[@text='Всі']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='Активні']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='Виконані']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='➕ Додати задачу']")))
    }

    @Test
    @DisplayName("2. Додавання нового завдання до списку")
    fun testAddNewTask() {
        val taskTitle = "Задача_${System.currentTimeMillis()}"

        val addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='➕ Додати задачу']")))
        addButton.click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Створити задачу']")))

        val inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")))
        inputField.sendKeys(taskTitle)

        driver.findElement(By.xpath("//*[@text='💾 Зберегти']")).click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
    }

    @Test
    @DisplayName("3. Редагування існуючого завдання")
    fun testEditExistingTask() {
        val timestamp = System.currentTimeMillis()
        val originalTitle = "Оригінал_$timestamp"
        val updatedTitle = "Редаговано_$timestamp"

        createTask(originalTitle)

        val taskElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='$originalTitle']")))
        taskElement.click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Редагувати']")))

        val inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")))
        inputField.clear()
        inputField.sendKeys(updatedTitle)

        driver.findElement(By.xpath("//*[@text='💾 Зберегти']")).click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$updatedTitle']")))
    }

    @Test
    @DisplayName("4. Позначення завдання як виконаного")
    fun testToggleTaskCompletionStatus() {
        val taskTitle = "Чекбокс_${System.currentTimeMillis()}"
        createTask(taskTitle, isCompleted = false)

        // 1. Перевіряємо, що спочатку завдання відображається у фільтрі "Активні"
        driver.findElement(By.xpath("//*[@text='Активні']")).click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))

        // 2. Повертаємося до списку "Всі" та переходимо на екран редагування
        driver.findElement(By.xpath("//*[@text='Всі']")).click()
        val taskElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='$taskTitle']")))
        taskElement.click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Редагувати']")))

        // 3. Відмічаємо "Виконано"
        toggleEditScreenCheckbox(true)
        driver.findElement(By.xpath("//*[@text='💾 Зберегти']")).click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='ToDo List']")))

        // 4. Перевіряємо, що завдання перейшло у розділ "Виконані"
        driver.findElement(By.xpath("//*[@text='Виконані']")).click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='$taskTitle']")), "Завдання має відображатися у списку 'Виконані'")

        // 5. Перевіряємо, що завдання зникло з розділу "Активні"
        driver.findElement(By.xpath("//*[@text='Активні']")).click()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
        assertFalse(isElementPresent(By.xpath("//*[@text='$taskTitle']")), "Виконане завдання має зникнути зі списку 'Активні'")

        // 6. Повертаємо фільтр "Всі"
        driver.findElement(By.xpath("//*[@text='Всі']")).click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
    }

    @Test
    @DisplayName("5. Фільтрація завдань за статусом (Всі, Активні, Виконані)")
    fun testFilterTasksByStatus() {
        val timestamp = System.currentTimeMillis()
        val activeTaskTitle = "Акт_$timestamp"
        val completedTaskTitle = "Вик_$timestamp"

        createTask(activeTaskTitle, isCompleted = false)
        createTask(completedTaskTitle, isCompleted = true)

        // Фільтр: Активні
        driver.findElement(By.xpath("//*[@text='Активні']")).click()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@text='$completedTaskTitle']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='$activeTaskTitle']")))
        assertFalse(isElementPresent(By.xpath("//*[@text='$completedTaskTitle']")))

        // Фільтр: Виконані
        driver.findElement(By.xpath("//*[@text='Виконані']")).click()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@text='$activeTaskTitle']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='$completedTaskTitle']")))
        assertFalse(isElementPresent(By.xpath("//*[@text='$activeTaskTitle']")))

        // Фільтр: Всі
        driver.findElement(By.xpath("//*[@text='Всі']")).click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$activeTaskTitle']")))
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$completedTaskTitle']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='$activeTaskTitle']")))
        assertTrue(isElementPresent(By.xpath("//*[@text='$completedTaskTitle']")))
    }

    @Test
    @DisplayName("6. Видалення завдання зі списку")
    fun testDeleteTask() {
        val taskTitle = "Видалити_${System.currentTimeMillis()}"
        createTask(taskTitle)

        val deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='$taskTitle']/../..//*[@content-desc='Видалити']")))
        deleteButton.click()

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
        assertFalse(isElementPresent(By.xpath("//*[@text='$taskTitle']")))
    }

    @Test
    @DisplayName("7. Скасування створення завдання")
    fun testCancelTaskCreation() {
        val taskTitle = "Скасовано_${System.currentTimeMillis()}"

        val addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='➕ Додати задачу']")))
        addButton.click()

        val inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")))
        inputField.sendKeys(taskTitle)

        driver.findElement(By.xpath("//*[@text='Скасувати']")).click()

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='ToDo List']")))
        assertFalse(isElementPresent(By.xpath("//*[@text='$taskTitle']")))
    }

    @Test
    @DisplayName("8. Встановлення планованої дати та часу для завдання")
    fun testSetTaskDateAndTime() {
        val taskTitle = "ДатаЧас_${System.currentTimeMillis()}"

        val addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='➕ Додати задачу']")))
        addButton.click()

        val inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")))
        inputField.sendKeys(taskTitle)

        // 1. Вибір дати
        driver.findElement(By.xpath("//*[@text='Виберіть дату']")).click()
        val dayElements = driver.findElements(By.xpath("//*[@text='15' or @text='20' or @text='10' or @text='1']"))
        if (dayElements.isNotEmpty()) {
            dayElements[0].click()
        }
        val okDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='ОК' or @text='OK']")))
        okDateButton.click()

        // 2. Час початку
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='Початок']"))).click()
        val okStartTimeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='ОК' or @text='OK']")))
        okStartTimeButton.click()

        // 3. Час закінчення
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='Кінець']"))).click()
        val okEndTimeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='ОК' or @text='OK']")))
        okEndTimeButton.click()

        // 4. Збереження
        driver.findElement(By.xpath("//*[@text='💾 Зберегти']")).click()

        // 5. Перевірка результату на головному екрані
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='$taskTitle']")))
    }

    private fun createTask(title: String, isCompleted: Boolean = false) {
        val addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='➕ Додати задачу']")))
        addButton.click()

        val inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText")))
        inputField.sendKeys(title)

        if (isCompleted) {
            toggleEditScreenCheckbox(true)
        }

        driver.findElement(By.xpath("//*[@text='💾 Зберегти']")).click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='ToDo List']")))
    }

    @AfterEach
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }
}
