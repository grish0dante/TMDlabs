package com.example.todolist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todolist.model.Todo
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TodoItem(
    todo: Todo,
    onEditClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onStatusChange: (Boolean) -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEditClick(todo.id) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = todo.title,
                color = if (todo.completed) Color.Gray else Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (todo.completed) TextDecoration.LineThrough else TextDecoration.None
                )
            )

            if (todo.plannedDate != null || todo.plannedStartTime != null || todo.plannedEndTime != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    todo.plannedDate?.let { date ->
                        Text(
                            text = dateFormatter.format(date),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    if (todo.plannedStartTime != null || todo.plannedEndTime != null) {
                        if (todo.plannedDate != null) {
                            Text(" • ", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                        Text(
                            text = "${todo.plannedStartTime?.let { timeFormatter.format(it) } ?: "?"} - " +
                                    (todo.plannedEndTime?.let { timeFormatter.format(it) } ?: "?"),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = todo.completed,
                onCheckedChange = onStatusChange
            )

            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Видалити")
            }
        }
    }
}