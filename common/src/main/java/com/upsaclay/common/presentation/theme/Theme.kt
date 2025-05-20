package com.upsaclay.common.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val lightColorScheme = lightColorScheme(
    primary = primaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    onPrimary = onPrimaryLight,
    secondary = secondaryLight,
    secondaryContainer = secondaryContainerLight,
    tertiary = tertiaryLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    error = errorLight,
    surface = surfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurface = onSurfaceLight,
    onSurfaceVariant = onSurfaceVariantLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight
)

private val darkColorScheme = darkColorScheme(
    primary = primaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    onPrimary = onPrimaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    error = errorDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark
)

@Composable
fun GedoiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }
}