package cl.duoc.oportunidades.ui.oportunidad

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.oportunidades.datos.local.OportunidadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOportunidadesScreen(
    vm: OportunidadViewModel, alAgregar: () -> Unit, alEditar: (Long) -> Unit, alVolver: () -> Unit
) {
    val lista by vm.oportunidades.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oportunidades (${lista.size})") },
                navigationIcon = { IconButton(onClick = alVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver al menú") } }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = alAgregar, icon = { Icon(Icons.Filled.Add, null) }, text = { Text("Nueva oportunidad") })
        }
    ) { pad ->
        if (lista.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(pad), Arrangement.Center, Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Lightbulb, null, Modifier.size(80.dp), tint = MaterialTheme.colorScheme.outline)
                Text("Aún no hay oportunidades registradas", style = MaterialTheme.typography.titleMedium)
                Text("Toca «Nueva oportunidad» para crear la primera")
            }
        } else {
            LazyColumn(Modifier.padding(pad), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(lista, key = { it.id }) { o -> TarjetaOportunidad(o, { alEditar(o.id) }, Modifier.animateItem()) }
            }
        }
    }
}

@Composable
private fun TarjetaOportunidad(o: OportunidadEntity, alClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorObjetivo = when (o.prioridad) {
        "Alta" -> Color(0xFFE53935); "Media" -> Color(0xFFF9A825); else -> Color(0xFF43A047)
    }
    val color by animateColorAsState(colorObjetivo, label = "colorPrioridad")
    Card(onClick = alClick, modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(o.nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Edit, "Editar", Modifier.size(20.dp))
            }
            Text(o.tipo, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AssistChip(
                    onClick = alClick, label = { Text("Prioridad ${o.prioridad}") },
                    colors = AssistChipDefaults.assistChipColors(labelColor = color)
                )
                AssistChip(onClick = alClick, label = { Text(o.estado) })
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (o.fotoPath != null) Icon(Icons.Filled.PhotoCamera, "Con foto", Modifier.size(18.dp))
                if (o.latitud != null) Icon(Icons.Filled.LocationOn, "Con ubicación", Modifier.size(18.dp))
            }
        }
    }
}
