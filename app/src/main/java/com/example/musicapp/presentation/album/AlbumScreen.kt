package com.example.musicapp.presentation.album

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.example.musicapp.domain.model.Album
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.BottomPadding
import com.example.musicapp.presentation.common.ErrorScreen
import com.example.musicapp.presentation.common.SongItem
import com.example.musicapp.presentation.common.SongItemShimmer
import com.example.musicapp.ui.theme.Green
import kotlinx.coroutines.flow.Flow

@Composable
fun AlbumScreen(
    album: Album,
    onLoad: () -> Unit,
    state: AlbumUiState,
    onBack: () -> Unit,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {
    LaunchedEffect(Unit) {
        onLoad()
    }
    val maxHeaderHeight = 380.dp
    val minHeaderHeight = 100.dp

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
        LazyColumn(state = scrollState) {
            item { Spacer(modifier = Modifier.height(maxHeaderHeight)) }
            when(state) {
                is AlbumUiState.Success -> {
                    val tracks = state.albumTracks
                    tracks(
                        tracks = tracks,
                        onSongClick = onSongClick,
                        onLike = onLike,
                        onUnlike = onUnlike,
                        isLiked = isLiked
                    )

                }
                is AlbumUiState.Loading -> loadingScreen()
                is AlbumUiState.Error -> item{ErrorScreen()}
            }
        }
        CollapsingAlbumBar(
            offset = offset.value,
            maxHeight = maxHeaderHeight,
            minHeight = minHeaderHeight,
            onBackClick = onBack,
            imageUrl = album.image,
            title = album.name
        )
    }

}

fun LazyListScope.loadingScreen(modifier: Modifier = Modifier) {
    item {
        Column(modifier = modifier.padding(horizontal = 12.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            repeat(10) {
                SongItemShimmer()
            }
        }
    }
}

fun LazyListScope.tracks(
    tracks: List<Song>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {
    items(tracks.size) {
        val liked by isLiked(tracks[it].id).collectAsState(initial = false)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 10.dp)) {
            SongItem(
                modifier = Modifier.weight(1f),
                song = tracks[it],
                onClick = { onSongClick(tracks, it) },
                hideImage = true
            )
            IconButton(
                onClick = {
                    if (liked) {
                        onUnlike(tracks[it].id)
                    } else {
                        onLike(tracks[it])
                    }
                },
                modifier = Modifier.size(20.dp)
            ) { Icon(
                painter = if (liked) painterResource(R.drawable.check_circle) else  painterResource(R.drawable.add_circle),
                contentDescription = null,
                tint = if(liked) Green else Color.White.copy(alpha = 0.7f)) }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.size(20.dp)
            ) { Icon(painterResource(R.drawable.more_horiz), null, tint = Color.White.copy(alpha = 0.7f)) }


        }

    }
    item {
        Spacer(modifier = Modifier.height(BottomPadding))
    }
}