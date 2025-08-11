package com.greenland.collabalarm.ui.theme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun CollabTheme(amoled: Boolean, content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    val dark = true // force dark-first
    val dynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val scheme = when {
        dynamic && dark -> dynamicDarkColorScheme(ctx)
        amoled -> darkColorScheme().copy(surface = Color.Black, background = Color.Black)
        else -> darkColorScheme()
    }
    MaterialTheme(colorScheme = scheme, typography = Typography(), content = content)
}
