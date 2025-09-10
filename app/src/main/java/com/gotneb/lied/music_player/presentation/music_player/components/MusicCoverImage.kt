package com.gotneb.lied.music_player.presentation.music_player.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.gotneb.lied.R


@Composable
fun MusicCoverImage(
    coverPath: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = coverPath,
        contentDescription = "Music cover",
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.music_cover_placeholder),
        error = painterResource(R.drawable.music_cover_placeholder),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    )
}