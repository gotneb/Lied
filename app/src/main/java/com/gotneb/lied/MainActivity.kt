package com.gotneb.lied

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gotneb.lied.music_player.presentation.music_list.MusicListScreen
import com.gotneb.lied.music_player.presentation.music_list.MusicListViewModel
import com.gotneb.lied.ui.theme.LiedTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiedTheme {
                val viewModel = koinViewModel<MusicListViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                MusicListScreen(
                    state = state,
                    onAction = viewModel::onAction,
                )
            }
        }
    }
}