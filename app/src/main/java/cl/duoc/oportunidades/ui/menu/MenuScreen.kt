package cl.duoc.oportunidades.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.oportunidades.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(auth: AuthViewModel, alAbrirOportunidades: () -> Unit, alCerrarSesion: () -> Unit) {
    val usuario by auth.usuario.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Hola, ${usuario?.nombre?.substringBefore(' ').orEmpty()}") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            actions = { IconButton(onClick = { auth.cerrarSesion(); alCerrarSesion() }) { Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar sesión") } }
        )
    }) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Menú principal", style = MaterialTheme.typography.titleLarge)
            ItemMenu("Oportunidades", "Registrar, consultar y editar", Icons.Filled.Lightbulb, true, alAbrirOportunidades)
            ItemMenu("Eventos", "Próximamente", Icons.Filled.Event, false) {}
            ItemMenu("Organizaciones", "Próximamente", Icons.Filled.Groups, false) {}
            ItemMenu("Contactos", "Próximamente", Icons.Filled.Contacts, false) {}
        }
    }
}

@Composable
private fun ItemMenu(titulo: String, detalle: String, icono: ImageVector, activo: Boolean, alClick: () -> Unit) {
    Card(
        onClick = alClick, enabled = activo, modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (activo) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(icono, null, Modifier.size(36.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(detalle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
