package com.example.musicapp.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.domain.model.Artist
import com.example.musicapp.domain.model.SearchItem
import com.example.musicapp.domain.model.Song
import com.example.musicapp.presentation.Dimensions.BottomPadding
import com.example.musicapp.presentation.common.ArtistItem
import com.example.musicapp.presentation.common.SongItem
import com.example.musicapp.presentation.common.SongItemShimmer
import com.example.musicapp.presentation.common.CategoryButton
import com.example.musicapp.presentation.common.ErrorScreen
import com.example.musicapp.presentation.common.SectionTitle
import com.example.musicapp.ui.theme.Green
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    clearSearchHistory: () -> Unit,
    history: List<SearchItem>,
    state: SearchUiState,
    filter: SearchFilter,
    query: String,
    onChangeFilter: (SearchFilter) -> Unit,
    onQueryChange: (String) -> Unit,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artist: Artist) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>,
    onRetry: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(8.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFFDCDCDC),
                    fontSize = 14.sp,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = query,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = remember { MutableInteractionSource() },
                        placeholder = { Text("Search...", fontSize = 14.sp, color = Color.Gray) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp), tint = Color.White.copy(0.8f))
                        },
                        shape = CircleShape,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF2D2D2D),
                            unfocusedContainerColor = Color(0xFF2D2D2D),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    )
                }
            )
            if (query.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Clear,
                        null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp).clickable{onQueryChange("")}
                    )
            }


        }

        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(5.dp)) {
            SearchFilter.entries.forEach { category ->
                CategoryButton(
                    text = category.toString(),
                    isSelected = filter == category,
                    onClick = {
                        onChangeFilter(category)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

            if (state.isLoading) LoadingScreen()
            if (!state.isLoading && state.items.isEmpty() && state.errorMessage == null && query.isEmpty()) HistoryScreen(
                history = history,
                clearSearchHistory = clearSearchHistory,
                onSongClick = onSongClick,
                onArtistClick = onArtistClick,
                onLike = onLike,
                onUnlike = onUnlike,
                isLiked = isLiked
            )
            if (!state.isLoading && state.items.isEmpty() && state.errorMessage == null && query.isNotEmpty()) TextScreen("No results found.")
            if (state.errorMessage != null) {
                ErrorScreen(
                    text = state.errorMessage,
                    onRetry = {onRetry()}
                )
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(state.items) { index, item ->
                    when(item) {
                        is SearchUiItem.ArtistItem ->{

                                ArtistItem(
                                    artist = item.artist,
                                    onClick = {
                                        onArtistClick(item.artist)
                                    })
                        }
                        is SearchUiItem.SongItem -> {
                            val liked by isLiked(item.track.id).collectAsState(initial = false)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 5.dp)) {
                                SongItem(
                                    modifier = Modifier.weight(1f),
                                    song = item.track,
                                    onClick = {
                                        val songsOnly = state.items.filterIsInstance<SearchUiItem.SongItem>().map { it.track }
                                        val songIndex = songsOnly.indexOf(item.track)
                                        onSongClick(songsOnly, songIndex)
                                    }
                                )
                                IconButton(
                                    onClick = {
                                        if (liked) {
                                            onUnlike(item.track.id)
                                        } else {
                                            onLike(item.track)
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
                item {Spacer(modifier = Modifier.height(BottomPadding))}
            }
        }
    }

@Composable
fun HistoryScreen(
    clearSearchHistory: () -> Unit,
    history: List<SearchItem>,
    onSongClick: (List<Song>, index: Int) -> Unit,
    onArtistClick: (artist: Artist) -> Unit,
    onLike: (Song) -> Unit,
    onUnlike: (String) -> Unit,
    isLiked: (String) -> Flow<Boolean>
) {
    if (history.isEmpty()) {
        TextScreen("Your search history will appear here.")
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    SectionTitle("Search History", modifier = Modifier.weight(1f))
                    IconButton(onClick = {clearSearchHistory()}) {
                        Icon(Icons.Default.Clear, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                    }
                }

            }
            itemsIndexed(history) { index, item ->
                when(item) {
                    is SearchItem.SongItem -> {
                        val liked by isLiked(item.song.id).collectAsState(initial = false)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 5.dp)
                        ) {
                            SongItem(
                                modifier = Modifier.weight(1f),
                                song = item.song,
                                onClick = {
                                    val songsOnly = history
                                        .filterIsInstance<SearchItem.SongItem>()
                                        .map { it.song }
                                    val songIndex = songsOnly.indexOf(item.song)
                                    onSongClick(songsOnly, songIndex)
                                }
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
                    is SearchItem.ArtistItem -> {
                        ArtistItem(
                            artist = item.artist,
                            onClick = { onArtistClick(item.artist) }
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(BottomPadding)) }
        }
    }


}

@Composable
fun LoadingScreen() {
    Column {
        repeat(20) {
            SongItemShimmer()
        }
    }
}

@Composable
fun TextScreen(text: String) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)))
    }
}