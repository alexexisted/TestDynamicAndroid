package com.alexa.testdynamicandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DynamicBlueDark,
    onPrimary = Color.Black,
    primaryContainer = DynamicBlue,
    onPrimaryContainer = Color.White,
    secondary = DynamicBlueDark,
    onSecondary = Color.Black,
    secondaryContainer = ChainBadgeBlue,
    onSecondaryContainer = DynamicBlueDark,
    tertiary = SuccessGreenLight,
    onTertiary = Color.Black,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = CardDark,
    onSurfaceVariant = Color(0xFFE5E7EB),
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorRedLight,
    onError = Color.Black,
    errorContainer = AccentRedBackground,
    onErrorContainer = ErrorRedLight
)

private val LightColorScheme = lightColorScheme(
    primary = DynamicBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlueBackground,
    onPrimaryContainer = DynamicBlueLight,
    secondary = DynamicBlue,
    onSecondary = Color.White,
    secondaryContainer = ChainBadgeBlue,
    onSecondaryContainer = DynamicBlue,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = AccentRedBackground,
    onErrorContainer = ErrorRed
)

@Composable
fun TestDynamicAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}