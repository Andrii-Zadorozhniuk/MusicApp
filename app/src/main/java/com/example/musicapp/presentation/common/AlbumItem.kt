package com.example.musicapp.presentation.common

import com.example.musicapp.domain.model.Album

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicapp.R
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.VerticalImageSize


@Composable
fun AlbumItem(
    album: Album,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.width(VerticalImageSize).padding(7.dp).clickable{onClick()}
    ) {
        AsyncImage(
            modifier = Modifier
                .size(VerticalImageSize),
            contentScale = ContentScale.Crop,
            model = ImageRequest.Builder(context)
                .data(album.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.no_cover),
            error = painterResource(R.drawable.no_cover),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = album.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = album.artistName,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            maxLines = 1
        )

    }
}


