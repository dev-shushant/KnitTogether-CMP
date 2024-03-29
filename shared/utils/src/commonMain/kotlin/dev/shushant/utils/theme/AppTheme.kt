package dev.shushant.utils.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.shushant.utils.dimens.DeviceConfiguration
import dev.shushant.utils.dimens.Dimensions
import dev.shushant.utils.dimens.LocalAppConfiguration

val darkmodeState = mutableStateOf(false)
val PlatformState = mutableStateOf(Platform.ANDROID)
val safeAreaState = mutableStateOf(PaddingValues())
val SafeArea = compositionLocalOf { safeAreaState }
val CurrentPlatform = compositionLocalOf { PlatformState }
val DarkMode = compositionLocalOf { darkmodeState }


internal val LightDefaultColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray95,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50,
)

/**
 * Dark default theme color scheme
 */
internal val DarkDefaultColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray10,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

/**
 * Light Android theme color scheme
 */
internal val LightAndroidColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = DarkGreen40,
    onSecondary = Color.White,
    secondaryContainer = DarkGreen90,
    onSecondaryContainer = DarkGreen10,
    tertiary = Teal40,
    onTertiary = Color.White,
    tertiaryContainer = Teal90,
    onTertiaryContainer = Teal10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkGreenGray99,
    onBackground = DarkGreenGray10,
    surface = DarkGreenGray99,
    onSurface = DarkGreenGray10,
    surfaceVariant = GreenGray90,
    onSurfaceVariant = GreenGray30,
    inverseSurface = DarkGreenGray20,
    inverseOnSurface = DarkGreenGray95,
    outline = GreenGray50,
)

/**
 * Dark Android theme color scheme
 */
internal val DarkAndroidColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Green20,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = DarkGreen80,
    onSecondary = DarkGreen20,
    secondaryContainer = DarkGreen30,
    onSecondaryContainer = DarkGreen90,
    tertiary = Teal80,
    onTertiary = Teal20,
    tertiaryContainer = Teal30,
    onTertiaryContainer = Teal90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkGreenGray10,
    onBackground = DarkGreenGray90,
    surface = DarkGreenGray10,
    onSurface = DarkGreenGray90,
    surfaceVariant = GreenGray30,
    onSurfaceVariant = GreenGray80,
    inverseSurface = DarkGreenGray90,
    inverseOnSurface = DarkGreenGray10,
    outline = GreenGray60,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    deviceConfiguration: DeviceConfiguration = DeviceConfiguration(),
    content: @Composable () -> Unit,
) {
    // Color scheme
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    // Gradient colors
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = Color.White,
        container = colorScheme.inverseOnSurface,
    )
    // Background theme
    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    val dimensions = when {
        deviceConfiguration.screenWidthDp <= 320 -> Dimensions.sw320
        deviceConfiguration.screenWidthDp <= 480 -> Dimensions.sw480
        deviceConfiguration.screenWidthDp <= 600 -> Dimensions.sw600
        else -> Dimensions.sw720
    }

    // Composition locals
    CompositionLocalProvider(
        LocalGradientColors provides defaultGradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
        LocalAppConfiguration provides deviceConfiguration,
        LocalAppDimens provides dimensions,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content,
        )
    }
}


internal val LocalAppDimens = staticCompositionLocalOf<Dimensions> {
    Dimensions.sw320
}

 object AppNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}


/**
 * Light Android gradient colors
 */
internal val LightAndroidGradientColors = GradientColors(container = DarkGreenGray95)

/**
 * Dark Android gradient colors
 */
internal val DarkAndroidGradientColors = GradientColors(container = Color.Black)

/**
 * Light Android background theme
 */
internal val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
internal val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)


internal fun supportsDynamicTheming() = false