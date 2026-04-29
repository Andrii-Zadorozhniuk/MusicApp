package com.example.musicapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.HistoryItem
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.BottomPadding
import com.example.musicapp.presentation.common.ErrorScreen
import com.example.musicapp.presentation.common.HistoryItemShimmer
import com.example.musicapp.presentation.common.SectionTitle
import com.example.musicapp.presentation.common.SongItem
import com.example.musicapp.presentation.common.SongItemShimmer
import com.example.musicapp.presentation.common.SongItemVertical
import com.example.musicapp.presentation.common.SongItemVerticalShimmer
import com.example.musicapp.presentation.home.components.HistoryArtistItem
import com.example.musicapp.presentation.home.components.HistorySongItem
import com.example.musicapp.presentation.home.components.ProfileTopBar
import com.example.musicapp.ui.theme.TextColor
import kotlin.math.ceil


@Composable
fun HomeScreen(
    recentHistory: List<HistoryItem>,
    newTrendingSongs: LazyPagingItems<Song>,
    recommendedSongs: LazyPagingItems<Song>,
    allTrendingSongs: LazyPagingItems<Song>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artist: Artist) -> Unit,
) {

    val isLoading = allTrendingSongs.itemCount == 0 ||
            newTrendingSongs.itemCount == 0 ||
            recommendedSongs.itemCount == 0 || allTrendingSongs.loadState.refresh is LoadState.Loading
            || newTrendingSongs.loadState.refresh is LoadState.Loading || recommendedSongs.loadState.refresh is LoadState.Loading
    when {
        isLoading -> LoadingScreen()
        allTrendingSongs.loadState.refresh is LoadState.Error &&
                allTrendingSongs.itemCount == 0 -> ErrorScreen(onRetry = {
                    allTrendingSongs.retry()
                    newTrendingSongs.retry()
                    recommendedSongs.retry()
                })
        else -> LoadedHomeScreen(
            recentHistory = recentHistory,
            allTrendingSongs = allTrendingSongs,
            newTrendingSongs = newTrendingSongs,
            recommendedSongs = recommendedSongs,
            onSongClick = onSongClick,
            onArtistClick = onArtistClick
        )
    }
}





@Composable
fun LoadedHomeScreen(
    recentHistory: List<HistoryItem>,
    newTrendingSongs: LazyPagingItems<Song>,
    recommendedSongs: LazyPagingItems<Song>,
    allTrendingSongs: LazyPagingItems<Song>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artist: Artist) -> Unit
) {
    val selectedCategory = remember { mutableStateOf("All") }

    LazyColumn(modifier = Modifier
        .padding(10.dp)
        .statusBarsPadding(),
    ) {
       item {
           Spacer(modifier = Modifier.height(8.dp))
           ProfileTopBar(selectedCategory)
           Spacer(modifier = Modifier.height(2.dp))
       }

        when(selectedCategory.value) {
            "All" -> {
                item {
                    if (recentHistory.isNotEmpty()) {
                        RecentHistory(
                            recentHistory = recentHistory,
                            onSongClick = onSongClick,
                            onArtistClick = onArtistClick
                        )
                    }
                }
                horizontalSection("Recommended For You", recommendedSongs, onSongClick)
                horizontalSection("Popular This Week", newTrendingSongs, onSongClick)
                verticalSection("Discover Picks For You", allTrendingSongs, onSongClick)
            }

            "Trending" -> {
                horizontalSection("Recommended For You", recommendedSongs, onSongClick)
                verticalSection("Popular This Week", newTrendingSongs, onSongClick)
            }

            "New Releases" -> {
                horizontalSection("New Releases For You", newTrendingSongs, onSongClick)
                verticalSection("Discover Picks For You", allTrendingSongs, onSongClick)
            }
        }

        item { Spacer(modifier = Modifier.height(BottomPadding)) }
    }
}


@Composable
fun RecentHistory(
    recentHistory: List<HistoryItem>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artist: Artist) -> Unit
) {
    val rows = ceil(recentHistory.size / 2f).toInt()
    val itemHeight = 48.dp
    val spacing = 8.dp
    val gridHeight = itemHeight * rows + spacing * (rows - 1) + spacing * 2
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier.height(gridHeight) .padding(spacing),
        userScrollEnabled = false
    ) {
        items(recentHistory) { item ->
            when(item) {
                is HistoryItem.ArtistItem -> {
                    HistoryArtistItem(
                        artist = item.artist,
                        onClick = {onArtistClick(item.artist)}
                    )
                }
                is HistoryItem.SongItem -> {
                    HistorySongItem(
                        song = item.song,
                        onClick = {onSongClick(listOf(item.song), 0)}
                    )
                }
            }
        }
    }
}

fun LazyListScope.horizontalSection(
    title: String,
    songs: LazyPagingItems<Song>,
    onSongClick: (List<Song>, Int) -> Unit
) {
    item {
        SectionTitle(title)
        LazyRow(
        ) {
            items(songs.itemCount) { index ->
                songs[index]?.let { song ->
                    SongItemVertical(
                        song = song,
                        onClick = {
                            val all = (0 until songs.itemCount).mapNotNull {songs[it]}
                            onSongClick(all, index)
                        }
                    )
                }
            }
            item {
                SongItemVerticalShimmer()
            }
        }
    }
    item {
        Spacer(modifier = Modifier.height(18.dp))
    }
}

fun LazyListScope.verticalSection(
    title: String,
    songs: LazyPagingItems<Song>,
    onSongClick: (List<Song>, Int) -> Unit
) {
    item {
        SectionTitle(title)
        Spacer(modifier = Modifier.height(5.dp))
    }
    items(songs.itemCount) { index ->
        songs[index]?.let { song ->
            SongItem(
                song = song,
                onClick = {
                    val all = (0 until songs.itemCount).mapNotNull {songs[it]}
                    onSongClick(all, index)
                }
            )
        }
    }
    items(3) { SongItemShimmer() }


}




@Composable
fun LoadingScreen() {
    Column(modifier = Modifier
        .padding(8.dp)
        .statusBarsPadding()) {
        ProfileTopBar()
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(180.dp) .padding(8.dp),
            userScrollEnabled = false
        ) {
            items(6) {
                HistoryItemShimmer()
            }
        }
        Text("Recommended For You", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
        LazyRow {
            items(10) {
                SongItemVerticalShimmer()
            }
        }
        Text("Popular This Week", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
        LazyRow {
            items(10) {
                SongItemVerticalShimmer()
            }
        }
        Text("Discover Picks For You", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
        Column {
            repeat(10) {
                SongItemShimmer()
            }
        }

    }
}



