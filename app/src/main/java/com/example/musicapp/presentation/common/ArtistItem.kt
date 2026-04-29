package com.example.musicapp.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.HorizontalImageSize

@Composable
fun ArtistItem(
    modifier: Modifier = Modifier,
    artist: Artist,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth().padding(10.dp).clickable{onClick()}
    ) {
        AsyncImage(
            modifier = Modifier
                .size(HorizontalImageSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(context)
                .data(artist.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.no_cover),
            error = painterResource(R.drawable.no_cover),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)){
            Text(
                text = artist.name,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 1
            )
            Text(
                text = "Artist",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                maxLines = 1
            )
        }

    }
}