package space.algoritmos.habit_tracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Dark theme
private val DarkColorScheme = darkColorScheme(
    primary = Purple40,
    secondary = Teal40,
    tertiary = Pink40,
    background = Gray90,
    surface = Color(0xFF2C2C2C),
    surfaceVariant = Color(0xFF373737),
    error = Color(0xFFCF6679),
    outline = Color(0xFFB0B0B0),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

// Light theme
private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    secondary = Teal40,
    tertiary = Pink40,
    background = Gray10,
    surface = Color.White,
    surfaceVariant = Color(0xFFE0E0E0),
    error = Color(0xFFB00020),
    outline = Color(0xFF707070),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)


@Composable
fun HabittrackerTheme(
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