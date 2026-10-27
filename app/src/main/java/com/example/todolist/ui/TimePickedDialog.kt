package com.example.todolist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Date) -> Unit,
    title: String = "Оберіть час"
) {
    val timeState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 0
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(Modifier.padding(16.dp)) {

                Text(title, style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(16.dp))

                TimePicker(
                    state = timeState,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) { Text("Скасувати") }

                    Spacer(Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, timeState.hour)
                            calendar.set(Calendar.MINUTE, timeState.minute)
                            onTimeSelected(calendar.time)
                        }
                    ) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}
