package com.example.finanx.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrownPrimaryDark,
    onPrimary = DarkBackground,
    secondary = GoldAccent,
    onSecondary = DarkBackground,
    tertiary = IncomeGreen,
    onTertiary = White,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = Color(0xFF332B23),
    error = ExpenseRed,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = BrownPrimary,
    onPrimary = White,
    secondary = BrownSecondary,
    onSecondary = White,
    tertiary = GoldAccent,
    onTertiary = BrownPrimary,
    background = WarmBackground,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = WarmBackground,
    outline = BorderSoft,
    error = ExpenseRed,
    onError = White
)

@Composable
fun FinanxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}