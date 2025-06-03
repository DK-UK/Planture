package com.dkdevs.testing2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Color Definitions
object PlantAppColors {
    // Light Theme Colors
    object Light {
        val Primary = Color(0xFF2E7D32)
        val OnPrimary = Color(0xFFFFFFFF)
        val PrimaryContainer = Color(0xFFA8DAB5)
        val OnPrimaryContainer = Color(0xFF002106)

        val Secondary = Color(0xFF526350)
        val OnSecondary = Color(0xFFFFFFFF)
        val SecondaryContainer = Color(0xFFD4E8D0)
        val OnSecondaryContainer = Color(0xFF101F10)

        val Background = Color(0xFFFCFDF6)
        val OnBackground = Color(0xFF1A1C18)
        val Surface = Color(0xFFFCFDF6)
        val OnSurface = Color(0xFF1A1C18)
        val SurfaceVariant = Color(0xFFDEE5D8)
        val OnSurfaceVariant = Color(0xFF424940)

        val Outline = Color(0xFF727970)
        val OutlineVariant = Color(0xFFC2C9BC)

        // Custom colors
        val CardBackground = Color(0xFFFFFFFF)
        val SearchBackground = Color(0xFFF5F5F5)
        val ItemBackground = Color(0xFFF8F9FA)
        val TextSecondary = Color(0xFF5F6368)
        val TextHint = Color(0xFF9AA0A6)
        val AccentGreen = Color(0xFF34A853)
        val ErrorRed = Color(0xFFD93025)
        val WarningOrange = Color(0xFFFF8F00)
        val IconPrimary = Color(0xFF34A853)
        val IconSecondary = Color(0xFF5F6368)
        val Border = Color(0xFFE0E0E0)
        val Divider = Color(0xFFF0F0F0)
    }

    // Dark Theme Colors
    object Dark {
        val Primary = Color(0xFF8BC34A)
        val OnPrimary = Color(0xFF003A00)
        val PrimaryContainer = Color(0xFF1B5E20)
        val OnPrimaryContainer = Color(0xFFA8DAB5)

        val Secondary = Color(0xFFB8CCB4)
        val OnSecondary = Color(0xFF233423)
        val SecondaryContainer = Color(0xFF394B37)
        val OnSecondaryContainer = Color(0xFFD4E8D0)

        val Background = Color(0xFF0F110C)
        val OnBackground = Color(0xFFE2E3DD)
        val Surface = Color(0xFF1A1C16)
        val OnSurface = Color(0xFFE2E3DD)
        val SurfaceVariant = Color(0xFF424940)
        val OnSurfaceVariant = Color(0xFFC2C9BC)

        val Outline = Color(0xFF8C9388)
        val OutlineVariant = Color(0xFF424940)

        // Custom colors for dark theme
        val CardBackground = Color(0xFF1E2E1F)
        val SearchBackground = Color(0xFF0D4C0F)
        val ItemBackground = Color(0xFF243326)
        val TextSecondary = Color(0xFFB8B8B8)
        val TextHint = Color(0xFF888888)
        val AccentGreen = Color(0xFF8BC34A)
        val ErrorRed = Color(0xFFE57373)
        val WarningOrange = Color(0xFFFFB74D)
        val IconPrimary = Color(0xFF81C784)
        val IconSecondary = Color(0xFFB0BEC5)
        val Border = Color(0xFF424242)
        val Divider = Color(0xFF303030)
    }
}

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PlantAppColors.Light.Primary,
    onPrimary = PlantAppColors.Light.OnPrimary,
    primaryContainer = PlantAppColors.Light.PrimaryContainer,
    onPrimaryContainer = PlantAppColors.Light.OnPrimaryContainer,
    secondary = PlantAppColors.Light.Secondary,
    onSecondary = PlantAppColors.Light.OnSecondary,
    secondaryContainer = PlantAppColors.Light.SecondaryContainer,
    onSecondaryContainer = PlantAppColors.Light.OnSecondaryContainer,
    background = PlantAppColors.Light.Background,
    onBackground = PlantAppColors.Light.OnBackground,
    surface = PlantAppColors.Light.Surface,
    onSurface = PlantAppColors.Light.OnSurface,
    surfaceVariant = PlantAppColors.Light.SurfaceVariant,
    onSurfaceVariant = PlantAppColors.Light.OnSurfaceVariant,
    outline = PlantAppColors.Light.Outline,
    outlineVariant = PlantAppColors.Light.OutlineVariant
)

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PlantAppColors.Dark.Primary,
    onPrimary = PlantAppColors.Dark.OnPrimary,
    primaryContainer = PlantAppColors.Dark.PrimaryContainer,
    onPrimaryContainer = PlantAppColors.Dark.OnPrimaryContainer,
    secondary = PlantAppColors.Dark.Secondary,
    onSecondary = PlantAppColors.Dark.OnSecondary,
    secondaryContainer = PlantAppColors.Dark.SecondaryContainer,
    onSecondaryContainer = PlantAppColors.Dark.OnSecondaryContainer,
    background = PlantAppColors.Dark.Background,
    onBackground = PlantAppColors.Dark.OnBackground,
    surface = PlantAppColors.Dark.Surface,
    onSurface = PlantAppColors.Dark.OnSurface,
    surfaceVariant = PlantAppColors.Dark.SurfaceVariant,
    onSurfaceVariant = PlantAppColors.Dark.OnSurfaceVariant,
    outline = PlantAppColors.Dark.Outline,
    outlineVariant = PlantAppColors.Dark.OutlineVariant
)


/*
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    */
/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    *//*

)
*/

@Composable
fun Testing2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
       /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
*/
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Custom theme colors accessor
val MaterialTheme.plantColors: PlantColors
    @Composable
    get() = if (isSystemInDarkTheme()) {
        PlantColors(
            cardBackground = PlantAppColors.Dark.CardBackground,
            searchBackground = PlantAppColors.Dark.SearchBackground,
            itemBackground = PlantAppColors.Dark.ItemBackground,
            textSecondary = PlantAppColors.Dark.TextSecondary,
            textHint = PlantAppColors.Dark.TextHint,
            accentGreen = PlantAppColors.Dark.AccentGreen,
            errorRed = PlantAppColors.Dark.ErrorRed,
            warningOrange = PlantAppColors.Dark.WarningOrange,
            iconPrimary = PlantAppColors.Dark.IconPrimary,
            iconSecondary = PlantAppColors.Dark.IconSecondary,
            border = PlantAppColors.Dark.Border,
            divider = PlantAppColors.Dark.Divider
        )
    } else {
        PlantColors(
            cardBackground = PlantAppColors.Light.CardBackground,
            searchBackground = PlantAppColors.Light.SearchBackground,
            itemBackground = PlantAppColors.Light.ItemBackground,
            textSecondary = PlantAppColors.Light.TextSecondary,
            textHint = PlantAppColors.Light.TextHint,
            accentGreen = PlantAppColors.Light.AccentGreen,
            errorRed = PlantAppColors.Light.ErrorRed,
            warningOrange = PlantAppColors.Light.WarningOrange,
            iconPrimary = PlantAppColors.Light.IconPrimary,
            iconSecondary = PlantAppColors.Light.IconSecondary,
            border = PlantAppColors.Light.Border,
            divider = PlantAppColors.Light.Divider
        )
    }

// Custom colors data class
data class PlantColors(
    val cardBackground: Color,
    val searchBackground: Color,
    val itemBackground: Color,
    val textSecondary: Color,
    val textHint: Color,
    val accentGreen: Color,
    val errorRed: Color,
    val warningOrange: Color,
    val iconPrimary: Color,
    val iconSecondary: Color,
    val border: Color,
    val divider: Color
)