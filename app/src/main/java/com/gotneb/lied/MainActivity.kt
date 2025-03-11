package com.gotneb.lied

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gotneb.lied.core.navigation.Route
import com.gotneb.lied.music_player.presentation.music_list.MusicListAction
import com.gotneb.lied.music_player.presentation.music_list.MusicListScreen
import com.gotneb.lied.music_player.presentation.music_list.MusicListViewModel
import com.gotneb.lied.music_player.presentation.music_player.MusicPlayerScreen
import com.gotneb.lied.ui.theme.LiedTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiedTheme {
                val navController = rememberNavController()

                val viewModel = koinViewModel<MusicListViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                NavHost(
                    navController = navController,
                    startDestination = Route.MusicList,
                ) {
                    composable<Route.MusicList> {
                        MusicListScreen(
                            state = state,
                            onAction = { action ->
                                viewModel.onAction(action)
                                if (action is MusicListAction.OnMusicClick) {
                                    navController.navigate(Route.MusicPlayer)
                                }
                            },
                        )
                    }
                    composable<Route.MusicPlayer>(
                        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
                        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right) },
                        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
                    ) {
                        MusicPlayerScreen(
                            state = state,
                            onAction = { /* TODO */ },
                        )
                    }
                }
            }
        }
    }
}