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
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    val state = rememberDatePickerState()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(Modifier.padding(16.dp)) {

                Text("Оберіть дату", style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(16.dp))

                DatePicker(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    title = null
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
                            state.selectedDateMillis?.let { millis ->
                                onDateSelected(Date(millis))
                            }
                            onDismissRequest()
                        }
                    ) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}
