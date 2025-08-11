package com.greenland.collabalarm.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF9EC1FF),
    background = Color(0xFF000000),
    surface = Color(0xFF111111),
)

@Composable
fun CollabTheme(amoled: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkScheme, content = content)
}
