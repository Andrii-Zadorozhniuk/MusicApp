package com.example.musicapp

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.musicapp.ui.theme.Template_projectTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.player.MusicService
import com.example.musicapp.presentation.player.MusicBar
import com.example.musicapp.presentation.navgraph.NavGraph
import com.example.musicapp.presentation.navgraph.Route
import com.example.musicapp.presentation.navigation.BottomNavBar
import com.example.musicapp.presentation.player.FullPlayerContent
import com.example.musicapp.ui.theme.Background
import com.example.musicapp.utils.LocalSongStatusProvider
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            Template_projectTheme {
                MainApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val playerState by viewModel.playerManager.state.collectAsState()
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val statusProvider: (String) -> SongStatus = remember(playerState) {
        { songId ->
            viewModel.songStatus(songId)
        }
    }
    CompositionLocalProvider(LocalSongStatusProvider provides statusProvider) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)) {
            NavGraph(
                navController = navController,
                onSongClick = viewModel::onSongClick,
                onLike = viewModel::likeSong,
                onUnlike = viewModel::unlikeSong,
                isLiked = viewModel::isLiked
            )
            Box(
                modifier = Modifier.align(Alignment.BottomCenter).background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.3f to Color.Transparent,
                            0.4f to Color.Black.copy(0.9f),
                            1.0f to Color.Black
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
            ) {
                Column {
                    MusicBar(
                        onLike = viewModel::likeSong,
                        onUnlike = viewModel::unlikeSong,
                        isLiked = viewModel::isLiked,
                        playerManager = viewModel.playerManager,
                        onOpenDetails = { isSheetOpen = true },
                    )
                    BottomNavBar(navController = navController)
                }
            }


        }
    }
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = Background,
            dragHandle = null
        ) {
            FullPlayerContent(
                onLike = viewModel::likeSong,
                onUnlike = viewModel::unlikeSong,
                isLiked = viewModel::isLiked,
                playerManager = viewModel.playerManager,
                onClose = {isSheetOpen = false},
                onArtistClick = {artistId ->
                    scope.launch {
                        isSheetOpen = false
                        val artist = viewModel.getArtist(artistId)
                        navController.currentBackStackEntry?.savedStateHandle?.set("artist", artist)
                        navController.navigate(route = Route.ArtistDetailsScreen.route)
                    }
                }
            )
        }
    }
}