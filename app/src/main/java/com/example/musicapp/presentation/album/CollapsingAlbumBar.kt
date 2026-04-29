package com.example.musicapp.presentation.album



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicapp.ui.theme.Background
import com.example.musicapp.utils.HelperFuncs

@Composable
fun CollapsingAlbumBar(
    offset: Float,
    imageUrl: String,
    title: String,
    maxHeight: Dp,
    minHeight: Dp,
    onBackClick: () -> Unit
) {
    val currentHeight = HelperFuncs().lerp(maxHeight, minHeight, offset)
    val isCollapsed = offset > 0.8f

    val backgroundColor = Background.copy(alpha = if (isCollapsed) 0.8f else offset)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(currentHeight)
            .background(backgroundColor)
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(currentHeight*0.6f)
                .alpha(1f - offset)
                .align(Alignment.Center)
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = if (isCollapsed) 0.dp else 20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(if (isCollapsed) Alignment.Center else Alignment.BottomStart)
                    .graphicsLayer {
                        val scale = HelperFuncs().lerp(1f, 0.75f, offset)
                        scaleX = scale
                        scaleY = scale
                    }
                    .padding(
                        bottom = if (isCollapsed) 0.dp else 16.dp,
                        start = if (isCollapsed) 8.dp else 5.dp
                    )
            )
        }


        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


