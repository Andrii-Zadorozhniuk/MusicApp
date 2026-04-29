package com.example.musicapp.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicapp.R
import com.example.musicapp.domain.model.Song
import com.example.musicapp.player.PlayerManager
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import com.example.musicapp.ui.theme.Green

@Composable
fun MusicBar(
    playerManager: PlayerManager,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {
    val state = playerManager.state.collectAsState().value
    val song = state.currentSong ?: return
    val liked by isLiked(song.id).collectAsState(initial = false)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 12.dp, end = 12.dp)
            .clickable { onOpenDetails() }
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF1E1E1E).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                AsyncImage(
                    model = song.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))


                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = song.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = song.artistName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }


                IconButton(
                    onClick = { playerManager.togglePlayPause() }
                ) {
                    if (state.isPlaying) {
                        Icon(
                            painterResource(R.drawable.pause),
                            contentDescription = null,
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                }

                IconButton(
                    onClick = {
                        if (liked) {
                            onUnlike(song.id)
                        } else {
                            onLike(song)
                        }
                    },
                    modifier = Modifier.size(20.dp)
                ) { Icon(
                    painter = if (liked) painterResource(R.drawable.check_circle) else  painterResource(R.drawable.add_circle),
                    contentDescription = null,
                    tint = if(liked) Green else Color.White.copy(alpha = 0.7f)) }
                Spacer(modifier = Modifier.width(4.dp))
            }

        }
        LinearProgressIndicator(
            progress = {state.progress},
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 24.dp)
                .height(2.dp).align(Alignment.BottomStart),
            color = Color.White,
            trackColor = Color.Transparent,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )

    }
}

