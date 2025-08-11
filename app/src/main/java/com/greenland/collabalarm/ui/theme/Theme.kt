package com.greenland.collabalarm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun CollabTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CollabDarkColors,
        typography = Typography(),
        content = content
    )
}
