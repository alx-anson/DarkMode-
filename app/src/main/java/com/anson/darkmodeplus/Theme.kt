package com.anson.darkmodeplus

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF121212),
    secondary = Color(0xFF1E1E1E),
    background = Color(0xFF121212),
    surface = Color(0xFF252525),
    onPrimary = Color(0xFFE0E0E0),
    onSecondary = Color(0xFFB0B0B0),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    outline = Color(0xFF333333),
    tertiary = Color(0xFFBB86FC) // Color de acento
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF121212),
    secondary = Color(0xFF1E1E1E),
    background = Color(0xFF121212),
    surface = Color(0xFF252525),
    onPrimary = Color(0xFFE0E0E0),
    onSecondary = Color(0xFFB0B0B0),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    outline = Color(0xFF333333),
    tertiary = Color(0xFFBB86FC) // Color de acento
)

@Composable
fun DarkModePlusTheme(
    dynamicColor: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    val view = LocalView.current

    val window = (view.context as Activity).window
    WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
    val config = LocalConfiguration.current

    val typography: Typography = if (config.screenHeightDp <= 712) {
        CompactSmallTypography

    } else {
        CompactTypography
    }

    AppUtils(appTypographyParam = typography) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

@Composable
fun AppUtils(
    appTypographyParam: Typography,
    content: @Composable () -> Unit
) {
    val appTypography = remember {
        appTypographyParam
    }

    CompositionLocalProvider(LocalAppTypography provides appTypography) {
        content()
    }
}

val LocalAppTypography = androidx.compose.runtime.compositionLocalOf {
    CompactTypography
}
val MaterialTheme.typo
    @Composable
    get() = LocalAppTypography.current