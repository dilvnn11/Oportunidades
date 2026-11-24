package cl.duoc.oportunidades.ui.oportunidad

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.oportunidades.datos.local.OportunidadEntity
import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOportunidadesScreen(
    vm: OportunidadViewModel, alAgregar: () -> Unit, alEditar: (Long) -> Unit, alVolver: () -> Unit
) {
    val lista by vm.oportunidades.collectAsStateWithLifecycle()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Oportunidades (${lista.size})") },
                navigationIcon = { IconButton(onClick = alVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver al menú") } },
                actions = {
                    IconButton(onClick = { vm.sincronizarConServidor(); vm.cargarIndicadoresExternos() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Sincronizar datos")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = alAgregar, icon = { Icon(Icons.Filled.Add, null) }, text = { Text("Nueva oportunidad") })
        }
    ) { pad ->
        LazyColumn(
            Modifier.padding(pad),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjeta de Indicadores de Mercado (API Externa)
            item {
                TarjetaIndicadoresMercado(uiState.indicadores, uiState.cargandoIndicadores)
            }

            if (lista.isEmpty()) {
                item {
                    Column(
                        Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Lightbulb, null, Modifier.size(80.dp), tint = MaterialTheme.colorScheme.outline)
                        Text("Aún no hay oportunidades registradas", style = MaterialTheme.typography.titleMedium)
                        Text("Toca «Nueva oportunidad» para crear la primera")
                    }
                }
            } else {
                items(lista, key = { it.id }) { o ->
                    TarjetaOportunidad(o, { alEditar(o.id) }, Modifier.animateItem())
                }
            }
        }
    }
}

@Composable
private fun TarjetaIndicadoresMercado(indicadores: MindicadorResponse?, cargando: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Indicadores de Mercado (API Externa)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            if (cargando) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            } else if (indicadores != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    indicadores.uf?.let { Text("UF: $${it.valor}", style = MaterialTheme.typography.bodySmall) }
                    indicadores.dolar?.let { Text("Dólar: $${it.valor}", style = MaterialTheme.typography.bodySmall) }
                    indicadores.euro?.let { Text("Euro: $${it.valor}", style = MaterialTheme.typography.bodySmall) }
                }
            } else {
                Text(
                    "Consulta de valores UF/Dólar lista para valoración comercial",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
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
