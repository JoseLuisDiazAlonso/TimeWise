package com.timewise.app.ui.taskform

/**Esta clase actúa como una foto fija, es decir lo que la pantalla necesita mostrar en un
 * instante. Esta clase se crea nantes que el ViewMovel pues se define en función de este estado.
 * */


import com.timewise.app.domain.model.Priority

import java.time.LocalDate
import java.time.LocalTime

data class TaskFormUiState (
    val id: Long? = null,
    val title: String = "",
    val titleError: Boolean = false,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val categoryOption: CategoryOption = availableCategories.first(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
)