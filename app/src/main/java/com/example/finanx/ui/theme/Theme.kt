package com.example.finanx.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val IconSizeContainer = 44.dp
val IconSizeIcon = 20.dp

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
    primaryContainer = GoldAccent,
    onPrimaryContainer = BrownPrimary,
    secondary = BrownSecondary,
    onSecondary = White,
    secondaryContainer = BorderSoft,
    onSecondaryContainer = BrownPrimary,
    tertiary = GoldAccent,
    onTertiary = BrownPrimary,
    tertiaryContainer = WarmBackground,
    onTertiaryContainer = BrownPrimary,
    background = WarmBackground,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = WarmBackground,
    onSurfaceVariant = StoneGray,
    surfaceContainerLowest = SurfaceWhite,
    surfaceContainerLow = WarmBackground,
    surfaceContainer = WarmBackground,
    surfaceContainerHigh = BorderSoft,
    surfaceContainerHighest = BorderSoft,
    outline = BorderSoft,
    outlineVariant = BorderSoft,
    error = ExpenseRed,
    onError = White,
    errorContainer = ExpenseRed.copy(alpha = 0.12f),
    onErrorContainer = ExpenseRed,
    scrim = BrownPrimary,
    inverseSurface = BrownPrimary,
    inverseOnSurface = WarmBackground,
    inversePrimary = GoldAccent
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