package com.timewise.app.ui.taskform

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.timewise.app.R
import com.timewise.app.ui.theme.CategoryHealth
import com.timewise.app.ui.theme.CategoryHome
import com.timewise.app.ui.theme.CategoryPersonal
import com.timewise.app.ui.theme.CategoryStudy
import com.timewise.app.ui.theme.CategoryWork

data class CategoryOption(
    val id: Int,
    @StringRes val labelRes: Int,
    val color: Color
)

val availableCategories = listOf(
    CategoryOption (id = 1, labelRes = R.string.category_work, color = CategoryWork),
    CategoryOption (id = 2, labelRes = R.string.category_personal, color = CategoryPersonal),
    CategoryOption (id = 3, labelRes = R.string.category_study, color = CategoryStudy),
    CategoryOption (id = 4, labelRes = R.string.category_health, color = CategoryHealth),
    CategoryOption (id = 5, labelRes = R.string.category_home, color = CategoryHome),
)
