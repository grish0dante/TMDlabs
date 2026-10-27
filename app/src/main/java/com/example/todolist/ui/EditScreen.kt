package com.example.todolist.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.model.Todo
import com.example.todolist.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditScreen(
    taskToEdit: Todo?,
    viewModel: TodoViewModel,
    navController: NavController
) {
    // Ініціалізація даними з переданого об'єкта
    var title by rememberSaveable { mutableStateOf(taskToEdit?.title ?: "") }
    var completed by rememberSaveable { mutableStateOf(taskToEdit?.completed ?: false) }

    var plannedDateMillis by rememberSaveable { mutableStateOf(taskToEdit?.plannedDate?.time) }
    var startTimeMillis by rememberSaveable { mutableStateOf(taskToEdit?.plannedStartTime?.time) }
    var endTimeMillis by rememberSaveable { mutableStateOf(taskToEdit?.plannedEndTime?.time) }

    val plannedDate = plannedDateMillis?.let { Date(it) }
    val startTime = startTimeMillis?.let { Date(it) }
    val endTime = endTimeMillis?.let { Date(it) }

    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showStartTimePicker by rememberSaveable { mutableStateOf(false) }
    var showEndTimePicker by rememberSaveable { mutableStateOf(false) }

    val onSave = {
        if (taskToEdit == null) {
            // Створення нової
            viewModel.addTask(title, plannedDate, startTime, endTime, completed)
        } else {
            // Оновлення наявної
            viewModel.updateTask(taskToEdit.id, title, plannedDate, startTime, endTime, completed)
        }
        navController.popBackStack()
    }


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val titleContent = @Composable {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Назва задачі") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = completed, onCheckedChange = { completed = it })
            Text("Виконано")
        }
    }

    val dateTimeContent = @Composable {
        Text("Планована дата та час:", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(plannedDate?.let { dateFormatter.format(it) } ?: "Виберіть дату")
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { showStartTimePicker = true }, modifier = Modifier.weight(1f)) {
                Text(startTime?.let { timeFormatter.format(it) } ?: "Початок")
            }
            OutlinedButton(onClick = { showEndTimePicker = true }, modifier = Modifier.weight(1f)) {
                Text(endTime?.let { timeFormatter.format(it) } ?: "Кінець")
            }
        }
    }

    val buttonsContent = @Composable {
        Row(Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) { Text("Скасувати") }
            Button(
                onClick = { onSave() },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) { Text("💾 Зберегти") }
        }
    }

    if (isLandscape) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(end = 16.dp)) {
                Text(if (taskToEdit == null) "Створити задачу" else "Редагувати", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                titleContent()
            }
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                dateTimeContent()
                buttonsContent()
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(if (taskToEdit == null) "Створити задачу" else "Редагувати", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))
            titleContent()
            Spacer(Modifier.height(16.dp))
            dateTimeContent()
            Spacer(Modifier.height(24.dp))
            buttonsContent()
        }
    }

    // Діалоги (Time/Date pickers) залишаються без змін, додайте їх сюди
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                plannedDateMillis = date.time
                showDatePicker = false
            }
        )
    }
    if (showStartTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            onTimeSelected = { time ->
                startTimeMillis = time.time
                showStartTimePicker = false
            },
            title = "Час початку"
        )
    }
    if (showEndTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            onTimeSelected = { time ->
                endTimeMillis = time.time
                showEndTimePicker = false
            },
            title = "Час завершення"
        )
    }
}