package com.timewise.app.ui.statistics.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.timewise.app.domain.model.CategoryTimeStats





/**
 * Esta clase es un Compasble que lo que hara es envolver el PieChart y mostrar su distribución
 * en minutos de la tarea por cada categoría del periodo actual.
 *
 * funciones.
 *  - StatsChartView (categoryStats: List<CategoryTimeStats>,
 *  modifier: Modifier = Modifier)**/

@Composable
fun StatsChartView(categoryStats: List<CategoryTimeStats>,
                   modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth().height(240.dp),
        factory = {context ->
            PieChart (context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                setHoleColor(android.graphics.Color.TRANSPARENT)
                setTransparentCircleAlpha(0)
                animateY(500)
            }

        },
        update = {chart ->
            val entries = categoryStats.map {stat ->
                PieEntry(stat.totalMinutes.toFloat(), "")
            }
            val colorInts = categoryStats.map {stat ->
                android.graphics.Color.parseColor(stat.categoryColorHex)
            }
            val dataSet = PieDataSet(entries, "").apply {
                colors = colorInts
                sliceSpace = 2f
            }
            chart.data = PieData(dataSet).apply { setDrawValues(false)
            chart.invalidate()
            }
        }
    )
}


