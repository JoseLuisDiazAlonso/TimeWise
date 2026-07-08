package com.timewise.app.ui.taskform.components

/**
 * PrioritySelector representa una función que selecciona entre un conjunto de prioridades
 * definidas en el enum de Task.kt.
 *
 * Lo que hace es iterar directamente sobre los valores del enum en lugar de escribir
 * una lista de prioridades a mano.*/

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp

import com.timewise.app.ui.theme.PriorityLow
import com.timewise.app.ui.theme.PriorityMedium
import com.timewise.app.ui.theme.PriorityHigh

import com.timewise.app.R


import com.timewise.app.domain.model.Priority


@Composable
fun PrioritySelector (
   selected: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp))  {

            Priority.entries.forEach { level ->
               FilterChip(
                   selected = level == selected,
                     onClick = { onPrioritySelected(level) },
                   label = {Text(text = stringResource(
                       when(level) {
                           Priority.LOW -> R.string.task_priority_low
                           Priority.MEDIUM -> R.string.task_priority_medium
                           Priority.HIGH -> R.string.task_priority_high
                   }))},
                     colors = FilterChipDefaults.filterChipColors (
                         selectedContainerColor = when(level) {
                                Priority.LOW -> PriorityLow
                                Priority.MEDIUM -> PriorityMedium
                                Priority.HIGH -> PriorityHigh
                         }
                     )
               )
            }

    }

}