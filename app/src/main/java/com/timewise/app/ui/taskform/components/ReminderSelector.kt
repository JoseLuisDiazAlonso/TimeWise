package com.timewise.app.ui.taskform.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.domain.model.ReminderOption

@Composable
fun ReminderSelector(
    selected: ReminderOption,
    onOptionSelected: (ReminderOption) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        enabled = enabled,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(stringResource(labelResFor(selected)))
        }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        ReminderOption.entries.forEach { option ->
            DropdownMenuItem(
                text = { Text(stringResource(labelResFor(option))) },
                onClick = {
                    onOptionSelected(option)
                    expanded = false
                }
            )
        }
    }
}

private fun labelResFor(option: ReminderOption): Int = when (option) {
    ReminderOption.NONE -> R.string.reminder_none
    ReminderOption.AT_TIME -> R.string.reminder_at_time
    ReminderOption.MIN_5 -> R.string.reminder_5_min
    ReminderOption.MIN_10 -> R.string.reminder_10_min
    ReminderOption.MIN_15 -> R.string.reminder_15_min
    ReminderOption.MIN_30 -> R.string.reminder_30_min
    ReminderOption.HOUR_1 -> R.string.reminder_1_hour
    ReminderOption.DAY_1 -> R.string.reminder_1_day
}