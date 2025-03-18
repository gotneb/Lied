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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
    currentDuration: Long,
    totalDuration: Long,
    height: Dp = 6.dp,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = currentDuration.toFloat(),
            onValueChange = {/* TODO */},
            valueRange = 0f..totalDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6739F1),
                activeTrackColor = Color(0xFF6739F1),
                inactiveTrackColor = Color(0xFFD9D9D9),
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
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
            currentDuration = 84000,
            totalDuration = 120000,
        )
    }
}