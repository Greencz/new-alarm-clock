package com.greenland.collabalarm.ui

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReadOnlyBanner() {
    Text("Viewer mode — read only", modifier = Modifier.fillMaxWidth().padding(8.dp))
}
