package com.timewise.app.ui.taskform.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.ui.taskform.CategoryOption
import com.timewise.app.ui.taskform.availableCategories

@Composable
fun CategorySelector(
    selected: CategoryOption,
    onCategorySelected: (CategoryOption) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(count = availableCategories.size) { index ->
            val category = availableCategories[index]
            FilterChip(
                modifier = Modifier.testTag("category_option_$index"),
                selected = category == selected,
                onClick = { onCategorySelected(category) },
                label = { Text(text = stringResource(id = category.labelRes)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = category.color
                )
            )
        }
    }
}