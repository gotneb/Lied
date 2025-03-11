package com.gotneb.lied.music_player.presentation.music_player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.gotneb.lied.R
import com.gotneb.lied.music_player.presentation.music_list.MusicListAction
import com.gotneb.lied.music_player.presentation.music_list.MusicListState
import com.gotneb.lied.music_player.presentation.music_list.components.musicPreview
import com.gotneb.lied.music_player.presentation.music_player.components.ProgressAudioBar
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun MusicPlayerScreen(
    state: MusicListState,
    onAction: (MusicListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(22.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            Row {
                IconButton(
                    onClick = { onAction(MusicListAction.OnGoBackClick) },
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Go back",
                        modifier = Modifier
                            .offset((-8).dp, 0.dp)
                            .size(32.dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.no_cover_music),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = state.currentMusic?.name ?: "No music")
                    Text(text = state.currentMusic?.singer ?: "No singer")
                }
                IconButton(onClick = {
                    state.currentMusic?.let { onAction(MusicListAction.OnFavoriteMusicClick(state.currentMusic.id)) }
                }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite music",
                    )
                }
            }
            ProgressAudioBar(
                value = 0.37f,
                currentDuration = state.currentDuration,
                totalDuration = state.totalDuration,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(onClick = { onAction(MusicListAction.OnShuffleClick) }) {
                    Icon(
                        painter = painterResource(R.drawable.shuffle),
                        contentDescription = "Select random music",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                IconButton(onClick = { onAction(MusicListAction.OnPreviousClick) }) {
                    Icon(
                        painter = painterResource(R.drawable.skip_inward),
                        contentDescription = "Play previous music",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                IconButton(
                    onClick = {
                        if (state.isPlaying) {
                            onAction(MusicListAction.OnPauseMusicClick)
                        } else {
                            onAction(MusicListAction.OnNextMusicClick)
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                ) {
                    Icon(
                        painter = painterResource(if (state.isPlaying) R.drawable.pause else R.drawable.play),
                        contentDescription = if (state.isPlaying) "Pause music" else "Play music",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
                IconButton(onClick = { onAction(MusicListAction.OnNextMusicClick) }) {
                    Icon(
                        painter = painterResource(R.drawable.skip_forward),
                        contentDescription = "Play next music",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                IconButton(onClick = { onAction(MusicListAction.OnRepeatClick) }) {
                    Icon(
                        painter = painterResource(R.drawable.repeat),
                        contentDescription = "Repeat music",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MusicPlayerScreenPreview() {
    LiedTheme {
        MusicPlayerScreen(
            state = MusicListState(currentMusic = musicPreview, isPlaying = true),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}