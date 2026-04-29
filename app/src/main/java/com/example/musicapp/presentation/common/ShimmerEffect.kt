package com.example.musicapp.presentation.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicapp.presentation.Dimensions.HorizontalImageSize
import com.example.musicapp.presentation.Dimensions.VerticalImageSize

fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition()
    val alpha = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        )
    ).value
    background(color = Color.DarkGray.copy(alpha))
}


@Composable
fun SongItemVerticalShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .width(VerticalImageSize)
            .padding(horizontal = 7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(VerticalImageSize)
                .aspectRatio(1f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(VerticalImageSize - 50.dp)
                .height(10.dp)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .width(VerticalImageSize - 80.dp)
                .height(10.dp)
                .shimmerEffect()
        )

    }
}

@Composable
fun SongItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(HorizontalImageSize)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column{
            Box(
                modifier = Modifier.height(12.dp).fillMaxWidth(0.6f).shimmerEffect()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier.height(10.dp).fillMaxWidth(0.3f).shimmerEffect()
            )
        }


    }
}

@Composable
fun HistoryItemShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10))
            .height(45.dp)
            .padding(end = 3.dp)
            .shimmerEffect()
    )
}

