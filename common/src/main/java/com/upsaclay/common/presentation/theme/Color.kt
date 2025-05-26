package com.upsaclay.common.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val white = Color(0xDEFFFFFF)
internal val black = Color(0xFF121212)
internal val lightGray = Color(0xFFCCCCCC)
internal val darkGray = Color(0xFF3C3C3C)

internal val primaryLight = Color(0xFF67708A)
internal val primaryContainerLight = Color(0xFFDDE0E7)
internal val onPrimaryContainerLight = Color(0xFF45474D)
internal val onPrimaryLight = white
internal val secondaryLight = Color(0xFF0B86FF)
internal val secondaryContainerLight = Color(0xFFD7DCE5)
internal val tertiaryLight = Color(0xFF009688)
internal val backgroundLight = Color(0xFFFFFFFF)
internal val onBackgroundLight = black
internal val errorLight = Color(0xFFED5245)
internal val surfaceLight = Color(0xFFFFFFFF)
internal val onSurfaceVariantLight = Color(0xFF4F4F4F)
internal val inverseSurfaceLight = Color(0xFF303133)
internal val inverseOnSurfaceLight = Color(0xFFEFF1F4)
internal val onSurfaceLight = black
internal val surfaceContainerHighLight = Color(0xFFE7E9EC)
internal val surfaceVariantLight = Color(0xFFE0E4EC)
internal val outlineLight = Color(0xFF74777E)
internal val outlineVariantLight = Color(0xFFC4C8D0)

internal val primaryDark = Color(0xFF67708A)
internal val primaryContainerDark = Color(0xFF444958)
internal val onPrimaryContainerDark = Color(0xFFE3E3E3)
internal val onPrimaryDark = white
internal val errorDark = Color(0xFFD64A4C)
internal val surfaceDark = Color(0xFF191919)
internal val surfaceContainerHighDark = Color(0xFF444958)
internal val surfaceVariantDark = Color(0xFF45484F)
internal val onSurfaceDark = white
internal val onSurfaceVariantDark = Color(0xFFD2D2D2)
internal val inverseSurfaceDark = white
internal val inverseOnSurfaceDark = Color(0xFF303133)
internal val onSecondaryContainerDark = white
internal val secondaryContainerDark = Color(0xFF444958)
internal val backgroundDark = Color(0xFF191919)
internal val onBackgroundDark = white
internal val outlineDark = Color(0xFF939393)
internal val outlineVariantDark = Color(0xFFC4C8D0)

val ColorScheme.black: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.black

val ColorScheme.white: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.white

val ColorScheme.lightGray: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.lightGray

val ColorScheme.darkGray: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.darkGray

val ColorScheme.gold: Color
    @Composable
    get() = Color(0xFFB98129)

val ColorScheme.inputBackground: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFEEEEEE)

val ColorScheme.inputForeground: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color(0xFFBEBEBE) else Color(0xFF646464)

val ColorScheme.cursor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) white else black

val ColorScheme.previewText: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color(0xFFA1A4B0) else Color(0xFF6F7181)

val ColorScheme.profilePictureError: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFE6E6E6)

val ColorScheme.hintText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF929298) else Color(0xFF8C8C8C)