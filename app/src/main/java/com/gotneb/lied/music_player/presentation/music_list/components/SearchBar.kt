package com.gotneb.lied.music_player.presentation.music_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.gotneb.lied.ui.theme.LiedTheme

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Search for a music...",
            )
        },
        maxLines = 1,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.clickable{ onSearchClick() }
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
    )
}

@PreviewLightDark
@Composable
private fun SearchBarPreview() {
    LiedTheme {
        SearchBar(
            value = "",
            onValueChange = {},
            onSearchClick = {},
        )
    }
}

