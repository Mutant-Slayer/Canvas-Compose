package com.example.canvas.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FillWaterBottle(modifier: Modifier = Modifier) {
    val totalWaterAmount by remember { mutableIntStateOf(2400) }
    var usedWaterAmount by remember { mutableIntStateOf(400) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        WaterBottle(totalWaterAmount = totalWaterAmount, usedWaterAmount = usedWaterAmount)
        Text(
            text = "Used Water: $usedWaterAmount L",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall
        )
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF279EFF)),
            onClick = { if (usedWaterAmount < totalWaterAmount) usedWaterAmount += 200 }) {
            Text(text = "Fill")
        }
    }
}

@Composable
fun WaterBottle(
    modifier: Modifier = Modifier,
    totalWaterAmount: Int = 2500,
    unit: String = "L",
    usedWaterAmount: Int = 200,
    waterColor: Color = Color(0xFF279EFF),
    bottleColor: Color = Color.White,
    capColor: Color = Color(0xFF0083EC)
) {
    val waterPercentage = animateFloatAsState(
        targetValue = (usedWaterAmount.toFloat() / totalWaterAmount.toFloat()),
        label = "Water waves animation",
        animationSpec = tween(1000)
    ).value

    val usedWaterAmountAnimation = animateIntAsState(
        targetValue = usedWaterAmount,
        label = "Used water animation",
        animationSpec = tween(1000)
    ).value

    Box(
        modifier = modifier
            .size(600.dp)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val gridPath = Path().apply {
                // Vertical lines
                for (i in 0..10) {
                    val x = width * i / 10f
                    moveTo(x, 0f)
                    lineTo(x, height)
                }
                // Horizontal lines
                for (i in 0..10) {
                    val y = height * i / 10f
                    moveTo(0f, y)
                    lineTo(width, y)
                }
            }

            drawPath(path = gridPath, style = Stroke(width = 1f), color = Color.Black)

            val bottleBodyPath = Path().apply {
                moveTo(width * 0.3f, height * 0f)
                lineTo(width * 0.3f, height * 0.2f)
                quadraticTo(
                    0f, height * 0.3f, // The pulling point
                    0f, height * 0.4f
                )
                lineTo(0f, height * 0.95f)
                quadraticTo(
                    0f, height,
                    width * 0.05f, height
                )

                lineTo(width * 0.95f, height)
                quadraticTo(
                    width, height,
                    width, height * 0.95f
                )
                lineTo(width, height * 0.4f)
                quadraticTo(
                    width, height * 0.3f,
                    width * 0.7f, height * 0.2f
                )
                lineTo(width * 0.7f, height * 0.2f)
                lineTo(width * 0.7f, height * 0f)
                close()
            }
            clipPath(
                path = bottleBodyPath
            ) {
                drawRect(color = bottleColor, size = size)

                val waterWavedYPosition = (1 - waterPercentage) * size.height
                val waterPath = Path().apply {
                    moveTo(x = 0f, y = waterWavedYPosition)
                    lineTo(x = size.width, y = waterWavedYPosition)
                    lineTo(x = size.width, y = size.height)
                    lineTo(x = 0f, y = size.height)
                    close()
                }
                drawPath(path = waterPath, color = waterColor)
            }

            drawRoundRect(
                color = capColor,
                size = Size(width = size.width * 0.5f, height = size.height * 0.07f),
                topLeft = Offset(x = size.width * 0.25f, y = 0f),
                cornerRadius = CornerRadius(x = 30f, y = 30f)
            )
        }
        val text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterColor,
                    fontSize = 44.sp
                )
            ) {
                append(usedWaterAmountAnimation.toString())
            }
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterColor,
                    fontSize = 22.sp
                )
            ) {
                append(" ")
                append(unit)
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text)
        }
    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
fun WaterBottlePathPreview() {
    WaterBottle(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(20.dp)
            .border(1.dp, Color.Red),
        totalWaterAmount = 1000,
        unit = "L",
        usedWaterAmount = 100,
        bottleColor = Color.Yellow
    )
}