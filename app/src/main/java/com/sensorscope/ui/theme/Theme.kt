package com.sensorscope.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkScheme = darkColorScheme(
    primary = Cyan400,
    secondary = Mint300,
    background = Slate900,
    surface = Slate800,
    onPrimary = Slate900,
    onBackground = Gray200,
    onSurface = Gray200
)

@Composable
fun SensorScopeTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = Typography,
        content = content
    )
}
