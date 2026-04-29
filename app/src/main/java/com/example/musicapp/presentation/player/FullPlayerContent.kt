package com.example.musicapp.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicapp.R
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.player.PlayerManager
import com.example.musicapp.presentation.Dimensions.IconSize
import com.example.musicapp.ui.theme.Background
import com.example.musicapp.ui.theme.Green
import com.example.musicapp.utils.HelperFuncs
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullPlayerContent(
    playerManager: PlayerManager,
    onClose: () -> Unit,
    onArtistClick: (artistId: String) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {
    val state = playerManager.state.collectAsState().value
    val song = state.currentSong ?: return
    val liked by isLiked(song.id).collectAsState(initial = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ){
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
            }

            Text(
                "Now Playing",
                color = Color.Gray,
                fontSize = 13.sp
            )
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.MoreVert, null, tint = Color.White)
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(16.dp)
                .statusBarsPadding()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(Modifier.height(40.dp))
            AsyncImage(
                model = song.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(song.name, color = Color.White, fontSize = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        modifier = Modifier.clickable { onArtistClick(song.artistId) },
                        text = song.artistName,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = {
                        if (liked) {
                            onUnlike(song.id)
                        } else {
                            onLike(song)
                        }
                    },
                    modifier = Modifier.size(28.dp)
                ) { Icon(
                    painter = if (liked) painterResource(R.drawable.check_circle) else  painterResource(R.drawable.add_circle),
                    contentDescription = null,
                    tint = if(liked) Green else Color.White.copy(alpha = 0.7f)) }
            }
            Spacer(Modifier.height(15.dp))
            Slider(
                value = state.position.toFloat(),
                onValueChange = { playerManager.seekTo(it.toLong()) },
                valueRange = 0f..state.duration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.4f)
                ),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(y = 4.dp)
                            .background(Color.White, CircleShape)
                    )
                },
                track = { sliderState ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.4f),
                                RoundedCornerShape(1.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(sliderState.value / state.duration)
                                .height(2.dp)
                                .background(
                                    Color.White,
                                    RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(HelperFuncs().formatTime(state.position), color = Color.Gray, fontSize = 14.sp)
                Text(HelperFuncs().formatTime(state.duration), color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {playerManager.previous()}) {
                    Icon(painter = painterResource(id = R.drawable.skip_previous), null, tint = Color.White,modifier = Modifier.size(IconSize))
                }
                Spacer(Modifier.width(10.dp))
                IconButton(onClick = {playerManager.togglePlayPause()}) {
                    if (state.isPlaying) {
                        Icon(painter = painterResource(id = R.drawable.pause), null, tint = Color.White, modifier = Modifier.size(IconSize))
                    } else {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(IconSize))
                    }
                }
                Spacer(Modifier.width(10.dp))
                IconButton(onClick = {playerManager.next()}) {
                    Icon(painter = painterResource(id = R.drawable.skip_next), null, tint = Color.White, modifier = Modifier.size(IconSize))
                }
            }

        }
    }

}