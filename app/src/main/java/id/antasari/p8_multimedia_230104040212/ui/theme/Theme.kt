package id.antasari.p8_multimedia_230104040212.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrandLightBlue,
    secondary = BrandDarkBlue,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = BrandLightBlue,     // Menggunakan Biru Terang sebagai warna utama
    secondary = BrandDarkBlue,    // Menggunakan Biru Tua sebagai sekunder
    background = BrandBackground, // Background aplikasi biru muda
    surface = SurfaceWhite,

    /* Other default colors to override */
    onPrimary = SurfaceWhite,
    onSecondary = SurfaceWhite,
    onBackground = TextDark,
    onSurface = TextDark,
)

@Composable
fun P8_multimedia_230104040212Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Ubah dynamicColor ke FALSE agar warna logo-mu yang dipakai, bukan warna wallpaper HP user
    dynamicColor: Boolean = false,
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