package com.gotneb.lied.music_player.presentation.music_list

import android.Manifest
import android.content.Intent
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
import androidx.compose.material3.Button
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
import com.gotneb.lied.R
import com.gotneb.lied.music_player.data.services.MusicPlayerService
import com.gotneb.lied.music_player.data.utils.MockUtils
import com.gotneb.lied.music_player.presentation.music_list.components.MusicListItem
import com.gotneb.lied.music_player.presentation.music_list.components.SearchBar
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun MusicListScreen(
    state: MusicListState,
    onAction: (MusicListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Permission launcher
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            onAction(MusicListAction.OnPermissionGranted)
        } else {
            onAction(MusicListAction.OnPermissionDenied)
        }
    }

    val context = LocalContext.current

    // Check and request permission when the screen is launched
    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf<String>()

        // Add storage permission based on Android version
        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        permissionsToRequest.add(storagePermission)

        // Add notification permission for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Check if all permissions are already granted
        val allPermissionsGranted = permissionsToRequest.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        if (allPermissionsGranted) {
            onAction(MusicListAction.OnPermissionGranted)
        } else {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    val musics = if (LocalInspectionMode.current) {
        remember { MockUtils.getMusics() }
    } else {
        state.currentPlaylist
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(MusicListAction.OnPlayRandomMusicClick) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.shuffle),
                    contentDescription = "Play a random music",
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
                    text = "${state.originalPlaylist.size} Songs",
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
                    onFavoriteClick = { musicId -> onAction(MusicListAction.OnFavoriteMusicClick(musicId)) }
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
