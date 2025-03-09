package com.gotneb.lied.music_player.presentation.music_player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gotneb.lied.core.presentation.timeToString
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun ProgressAudioBar(
    value: Float,
    height: Dp = 4.dp,
    currentDuration: Long,
    totalDuration: Long,
) {
    Column {
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(100))
        ) {
            // Background (unfilled part of the progress bar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(Color(0xFFD9D9D9))
            )
            // Filled part of the progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(value)
                    .height(height)
                    .background(Color(0xFF6739F1))
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = currentDuration.timeToString())
            Text(text = totalDuration.timeToString())
        }
    }
}

@PreviewLightDark
@Composable
private fun ProgressAudioBarPreview() {
    LiedTheme {
        ProgressAudioBar(
            value = 0.35f,
            currentDuration = 84000,
            totalDuration = 120000,
        )
    }
}