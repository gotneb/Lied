package com.gotneb.lied.music_player.presentation.music_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.gotneb.lied.music_player.presentation.music_list.components.MusicListItem
import com.gotneb.lied.music_player.presentation.music_list.components.SearchBar
import com.gotneb.lied.music_player.presentation.music_list.components.musicPreview
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun MusicListScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO() */ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Music",
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
                    value = "",
                    onValueChange = {},
                    onSearchClick = {},
                )
            }
            item {
                Text(
                    text = "17 Songs",
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            items(6) {
                MusicListItem(
                    music = musicPreview,
                    onMusicClick = {},
                    onFavoriteClick = {}
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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
