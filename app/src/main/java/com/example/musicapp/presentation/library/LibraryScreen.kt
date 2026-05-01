package com.example.musicapp.presentation.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.HistoryItem
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.common.CategoryButton
import com.example.musicapp.presentation.common.SectionTitle
import com.example.musicapp.presentation.common.SongItem
import com.example.musicapp.ui.theme.TextColor
import androidx.compose.runtime.setValue
import com.example.musicapp.presentation.Dimensions.BottomPadding
import com.example.musicapp.presentation.common.ArtistItem
import com.example.musicapp.presentation.common.SongItemShimmer
import com.example.musicapp.presentation.search.SearchUiItem
import com.example.musicapp.presentation.search.TextScreen
import com.example.musicapp.ui.theme.Green
import kotlinx.coroutines.flow.Flow

@Composable
fun LibraryScreen(
    likedSongs: List<Song>,
    libraryHistory: List<HistoryItem>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artistId: String) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>,
    isLoading: Boolean,
    goToSearch: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<LibraryCategories>(LibraryCategories.All)}
    val songsOnly = remember(libraryHistory) {
        libraryHistory.filterIsInstance<HistoryItem.SongItem>().map { it.song }
    }
        LazyColumn {
        item {
            TopBar(goToSearch)
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LibraryCategories.entries.forEach { category ->
                    CategoryButton(
                        text = category.name,
                        isSelected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                        }
                    )
                }
            }
            if (likedSongs.isNotEmpty() && (selectedCategory == LibraryCategories.All || selectedCategory == LibraryCategories.Liked)) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 14.dp)
                ) {

                    SectionTitle("Liked Songs (${likedSongs.size})", modifier = Modifier.weight(1f))


                    IconButton(
                        onClick = { selectedCategory  = LibraryCategories.Liked},
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

        }
            if (selectedCategory == LibraryCategories.All || selectedCategory == LibraryCategories.Liked) {
                when {
                    isLoading -> item{LoadingList()}
                    else -> {
                        items(likedSongs.size) {
                            val liked by isLiked(likedSongs[it].id).collectAsState(initial = false)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                SongItem(
                                    modifier = Modifier.weight(1f),
                                    song = likedSongs[it],
                                    onClick = {onSongClick(likedSongs, it)}
                                )
                                IconButton(
                                    onClick = {
                                        if (liked) {
                                            onUnlike(likedSongs[it].id)
                                        } else {
                                            onLike(likedSongs[it])
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
                    }
                }
            }

            if (selectedCategory == LibraryCategories.All || selectedCategory == LibraryCategories.History) {
        item{
            if (selectedCategory == LibraryCategories.All || selectedCategory == LibraryCategories.Liked) {
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (libraryHistory.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 14.dp)
            ) {
                SectionTitle("Your Recent History", modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {selectedCategory = LibraryCategories.History },
                ) { Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White, modifier = Modifier.size(32.dp)) }
            }}
        }

                when {
                    isLoading -> item{LoadingList()}

                    else -> {
                        items(libraryHistory) { item ->
                            when (item) {
                                is HistoryItem.ArtistItem -> {
                                    ArtistItem(
                                        artist = item.artist,
                                        onClick = { onArtistClick(item.artist.id) }
                                    )
                                }
                                is HistoryItem.SongItem -> {
                                    val songIndex = songsOnly.indexOf(item.song)
                                    val liked by isLiked(item.song.id).collectAsState(initial = false)
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                                        SongItem(
                                            modifier = Modifier.weight(1f),
                                            song = item.song,
                                            onClick = {onSongClick(songsOnly, songIndex)}
                                        )
                                        IconButton(
                                            onClick = {
                                                if (liked) {
                                                    onUnlike(item.song.id)
                                                } else {
                                                    onLike(item.song)
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
                            }


                        }
                    }
                }
            }

        item {
            Spacer(modifier = Modifier.height(BottomPadding))
        }
    }

}


@Composable
fun TopBar(goToSearch: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null, tint = TextColor,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color = Color.Gray.copy(alpha = 0.1f))
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SectionTitle("Your Library")
        }
        IconButton(
            onClick = {goToSearch()},
        ) { Icon(Icons.Default.Search, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
        IconButton(
            onClick = { /* TODO */ },
        ) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(26.dp)) }

    }
}

@Composable
fun LoadingList() {
    Column {
        repeat(8) {
            SongItemShimmer()
        }
    }
}

enum class LibraryCategories {
    All, Liked, History
}