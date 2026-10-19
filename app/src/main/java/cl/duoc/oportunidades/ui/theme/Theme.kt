package cl.duoc.oportunidades.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Claro = lightColorScheme(primary = Color(0xFF1565C0), secondary = Color(0xFF00897B), tertiary = Color(0xFFF9A825))
private val Oscuro = darkColorScheme(primary = Color(0xFF90CAF9), secondary = Color(0xFF80CBC4), tertiary = Color(0xFFFFCA28))

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Oscuro else Claro, content = content)
}
