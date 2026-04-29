package com.example.musicapp.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.SongStatus
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.HorizontalImageSize
import com.example.musicapp.ui.theme.Green
import com.example.musicapp.utils.LocalSongStatusProvider

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    onClick: () -> Unit,
    isBiggerIcon: Boolean = false,
    hideImage: Boolean = false
) {
    val context = LocalContext.current
    val getStatus = LocalSongStatusProvider.current
    val status = getStatus(song.id)
    Row(
        modifier = modifier.fillMaxWidth().padding(10.dp).clickable{onClick()}
    ) {
        if(!hideImage) {
            AsyncImage(
                modifier = Modifier
                    .size(if (isBiggerIcon) 65.dp else HorizontalImageSize),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(context)
                    .data(song.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.no_cover),
                error = painterResource(R.drawable.no_cover),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (status != SongStatus.NOT_PLAYING) {
                    MusicBarsAnimation(
                        modifier = Modifier.size(12.dp),
                        isPlaying = status == SongStatus.IS_PLAYING
                    )
                }
                Text(
                    text = song.name,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when(status) {
                        SongStatus.IS_PLAYING -> Green
                        SongStatus.PAUSED -> Green
                        SongStatus.NOT_PLAYING -> Color.White
                    },
                    maxLines = 1
                )
            }

            Text(
                text = song.artistName,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                maxLines = 1
            )
        }


    }
}