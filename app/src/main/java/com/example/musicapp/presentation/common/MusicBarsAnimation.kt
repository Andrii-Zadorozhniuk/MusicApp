package com.example.musicapp.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.musicapp.ui.theme.Green
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun MusicBarsAnimation(
    modifier: Modifier = Modifier,
    isPlaying: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition()

    val height1 by infiniteTransition.animateFloat(
        initialValue = 7f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val height2 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val height3 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Bar(height1, Green, isPlaying)
        Bar(height2, Green, isPlaying)
        Bar(height3, Green, isPlaying)
    }
}

@Composable
private fun Bar(height: Float, color: Color, isPlaying: Boolean) {
    val targetHeight = if (isPlaying) height else 3f
    Box(
        modifier = Modifier
            .width(2.dp)
            .height(targetHeight.dp)
            .clip(RoundedCornerShape(100))
            .background(color)
    )
}