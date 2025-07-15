package com.example.canvas.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Draw a graph with a canvas
 **/

@Composable
fun Graph() {
    Box(
        modifier = Modifier
            .background(color = Color.Magenta)
            .fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .offset(y = 200.dp)
                .fillMaxSize(),
        ) {
            drawRect(color = Color.DarkGray, style = Stroke(2.dp.toPx()))
            repeat(4) {
                val startX = ((size.width / 4) * it)
                drawLine(
                    color = Color.DarkGray,
                    strokeWidth = 2.dp.toPx(),
                    start = Offset(startX, 0f),
                    end = Offset(startX, size.height)
                )
            }
            repeat(4) {
                val startY = ((size.height / 4) * it)
                drawLine(
                    color = Color.DarkGray,
                    strokeWidth = 2.dp.toPx(),
                    start = Offset(0f, startY),
                    end = Offset(size.width, startY)
                )
            }
        }
    }
}