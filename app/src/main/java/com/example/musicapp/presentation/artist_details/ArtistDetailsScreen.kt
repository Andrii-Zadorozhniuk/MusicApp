package com.example.musicapp.presentation.artist_details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.BottomPadding
import com.example.musicapp.presentation.artist_details.components.CollapsingAppBar
import com.example.musicapp.presentation.common.AlbumItem
import com.example.musicapp.presentation.common.ErrorScreen
import com.example.musicapp.presentation.common.SongItem
import com.example.musicapp.presentation.common.SongItemShimmer
import com.example.musicapp.presentation.common.SongItemVerticalShimmer
import com.example.musicapp.ui.theme.Green
import com.example.musicapp.ui.theme.TextColor


@Composable
fun ArtistDetailsScreen(
    state: ArtistDetailsUiState,
    onBack: () -> Unit,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onAlbumClick: (Album) -> Unit) {
    val artist = (state as? ArtistDetailsUiState.Success)?.artist
    val maxHeaderHeight = 330.dp
    val minHeaderHeight = 80.dp

    val scrollState = rememberLazyListState()

    val offset = remember {
        derivedStateOf {
            val firstItemOffset = scrollState.firstVisibleItemScrollOffset.toFloat()
            val firstItemIndex = scrollState.firstVisibleItemIndex

            if (firstItemIndex > 0) 1f
            else (firstItemOffset / 500f).coerceIn(0f, 1f)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(maxHeaderHeight)) }
            when(state) {
               is ArtistDetailsUiState.Success -> {
                   val topSongs = state.artistTopSongs
                   val albums = state.artistAlbums
                   val artist = state.artist
                   details(
                       artist =artist,
                       topSongs = topSongs,
                       albums = albums,
                       onSongClick = onSongClick,
                       onAlbumClick = onAlbumClick
                   )
               }
               is ArtistDetailsUiState.Loading -> loadingScreen()
               is ArtistDetailsUiState.Error -> item{ErrorScreen()}
            }

        }

        CollapsingAppBar(
            offset = offset.value,
            maxHeight = maxHeaderHeight,
            minHeight = minHeaderHeight,
            onBackClick = {onBack()},
            imageUrl = artist?.image ?: "",
            title = artist?.name ?: ""
        )
    }

}


fun LazyListScope.loadingScreen(modifier: Modifier = Modifier) {
    item {
        Column(modifier = modifier.padding(horizontal = 12.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            repeat(5) {
                SongItemShimmer()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                repeat(3) {
                    SongItemVerticalShimmer()
                }
            }
        }
    }

}



fun LazyListScope.details(
    artist: Artist,
    topSongs: List<Song>,
    albums: List<Album>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onAlbumClick: (Album) -> Unit,
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = artist.joindate,
                color = Color.Gray,
                style = MaterialTheme.typography.labelLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = {},
                    border = BorderStroke(width = 1.2.dp, color = Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .sizeIn(minHeight = 1.dp, minWidth = 1.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Follow",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    onClick = {onSongClick(topSongs, 0)},
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.play_circle_filled),
                        contentDescription = "Back",
                        tint = Green,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        Text(
            text = "Popular",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
        )
    }
    items(topSongs.size) { index ->
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
            SongItem(
                modifier = Modifier.weight(1f).padding(start = 5.dp),
                song = topSongs[index],
                onClick = { onSongClick(topSongs, index) },
            )
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.size(20.dp)
            ) { Icon(painterResource(R.drawable.more_horiz), null, tint = Color.White.copy(alpha = 0.7f)) }
        }

    }
    item {
        Text(
            text = "Albums",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
        )
        LazyRow(modifier = Modifier.padding(horizontal = 5.dp)) {
            items(albums.size) {
                AlbumItem(
                    album = albums[it],
                    onClick = {onAlbumClick(albums[it])}
                )
            }
        }
        Spacer(modifier = Modifier.height(BottomPadding + 30.dp))
    }


}
