package com.timewise.app.ui.taskform.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timewise.app.ui.taskform.CategoryOption
import com.timewise.app.ui.taskform.availableCategories

import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource

/**Esta clase iterará a lo largo de las categorías de la lista especificada en la clase
 * CategoryOption
 * **/

@Composable
fun CategorySelector (
    selected: CategoryOption,
    onCategorySelected: (CategoryOption) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow (modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp))  {

            items(count = availableCategories.size) { index ->
                val category = availableCategories[index]
               FilterChip (
                   selected = category == selected,
                   onClick = { onCategorySelected(category) },
                   label = {
                       Text(text = stringResource(id = category.labelRes))
                   },
                   colors = FilterChipDefaults.filterChipColors (
                       selectedContainerColor = category.color
                   )
               )
            }
    }
}