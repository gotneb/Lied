package com.gotneb.lied.music_player.presentation.music_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.gotneb.lied.R
import com.gotneb.lied.core.presentation.timeToString
import com.gotneb.lied.music_player.domain.model.Music
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun MusicListItem(
    music: Music,
    onMusicClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onMusicClick(music.id) },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = music.coverRes),
                contentDescription = "Music cover",
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(48.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = music.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = music.singer,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, false)
                    )
                    Box(
                        Modifier
                            .width(4.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(100))
                            .background(color = MaterialTheme.colorScheme.onBackground)
                    )
                    Text(
                        text = music.duration.timeToString(),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite music",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { onFavoriteClick(music.id) }
                )
            }
        }
    }
}

internal val musicPreview = Music(
    id = 1,
    name = "Ginnme no Majo",
    singer = "Yatezy Records - Topic",
    duration = 174,
    isFavorite = false,
    coverRes = R.drawable.music_cover_placeholder,
)

@PreviewLightDark
@Composable
private fun MusicListItemPreview() {
    LiedTheme {
        MusicListItem(
            music = musicPreview,
            onMusicClick = {},
            onFavoriteClick = {},
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}