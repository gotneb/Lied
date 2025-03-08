package com.gotneb.lied.music_player.presentation.music_list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.gotneb.lied.music_player.data.utils.MockUtils
import com.gotneb.lied.music_player.presentation.music_list.components.MusicListItem
import com.gotneb.lied.music_player.presentation.music_list.components.SearchBar
import com.gotneb.lied.ui.theme.LiedTheme
import com.gotneb.lied.R

@Composable
fun MusicListScreen(
    state: MusicListState,
    onAction: (MusicListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onAction(MusicListAction.OnPermissionGranted)
        } else {
            onAction(MusicListAction.OnPermissionDenied)
        }
    }

    val context = LocalContext.current

    // Check and request permission when the screen is launched
    LaunchedEffect(Unit) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            onAction(MusicListAction.OnPermissionGranted)
        } else {
            permissionLauncher.launch(permission)
        }
    }

    val musics = if (LocalInspectionMode.current) {
        remember { MockUtils.getMusics() }
    } else {
        state.musics
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(MusicListAction.OnShuffleClick) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.shuffle),
                    contentDescription = "Play random music",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            item {
                Text(
                    text = "Songs",
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                SearchBar(
                    value = state.searchQuery,
                    onValueChange = { query -> onAction(MusicListAction.OnSearchQueryChange(query)) },
                    onSearchClick = { onAction(MusicListAction.OnSearchClick) },
                )
            }
            item {
                Text(
                    text = "${state.musics.size} Songs",
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            items(
                items = musics,
                key = { music -> music.id },
            ) { music ->
                MusicListItem(
                    music = music,
                    onMusicClick = { musicId -> onAction(MusicListAction.OnMusicClick(musicId)) },
                    onFavoriteClick = { musicId -> onAction(MusicListAction.OnMusicFavoriteClick(musicId)) }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MusicListScreenPreview() {
    LiedTheme {
        MusicListScreen(
            state = MusicListState(),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
