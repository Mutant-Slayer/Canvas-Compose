package com.example.canvas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.canvas.R
import kotlin.math.ceil

@Composable
fun StarRatingView(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingChanged: (newRating: Float) -> Unit = {},
    maxRating: Int = 5,
    enableDragging: Boolean = true,
    enableTapping: Boolean = true,
) {
    Box(
        modifier = modifier
            .then(
                if (enableDragging) {
                    Modifier
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                val width = size.width.toFloat()
                                val newRating = (change.position.x / width) * maxRating
                                onRatingChanged(newRating)
                            }
                        }
                } else {
                    Modifier
                }
            )
            .then(
                if (enableTapping) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val width = size.width.toFloat()
                            val rawRating = (offset.x / width) * maxRating
                            val newRating = ceil(rawRating)
                            onRatingChanged(newRating)
                        }
                    }
                } else {
                    Modifier
                }
            ),
    ) {
        UnratedStarContent(maxRating = maxRating)
        Box(
            modifier = Modifier
                .drawWithCache {
                    onDrawWithContent {
                        if (rating > 0) {
                            clipRect(
                                left = 0f,
                                top = 0f,
                                right = size.width * rating / maxRating,
                                bottom = size.height
                            ) {
                                this@onDrawWithContent.drawContent()
                            }
                        }
                    }
                }
        ) {
            RatedStarContent(
                modifier = Modifier,
                maxRating = maxRating
            )
        }
    }
}

@Composable
fun RatedStarContent(modifier: Modifier = Modifier, maxRating: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_rating_star_filled),
                contentDescription = "Rating star $i",
                modifier = Modifier
                    .size(48.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun UnratedStarContent(modifier: Modifier = Modifier, maxRating: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_rating_star_non_filled),
                contentDescription = "Rating star $i",
                modifier = Modifier
                    .size(48.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun RatedStarContentPreview() {
    Box(modifier = Modifier.background(Color.Black)) {
        RatedStarContent(
            modifier = Modifier.align(Alignment.Center),
            maxRating = 5
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun UnratedStarContentPreview() {
    Box(modifier = Modifier.background(Color.Black)) {
        UnratedStarContent(
            modifier = Modifier.align(Alignment.Center),
            maxRating = 5
        )
    }
}