package cl.duoc.oportunidades.ui.oportunidad

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.oportunidades.dominio.Opciones
import cl.duoc.oportunidades.ui.componentes.CampoSelector
import cl.duoc.oportunidades.ui.componentes.CampoValidado
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadFormScreen(vm: OportunidadViewModel, id: Long, alVolver: () -> Unit) {
    val ctx = LocalContext.current
    val s by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { vm.cargar(id) }
    LaunchedEffect(s.guardadoExitosamente) {
        if (s.guardadoExitosamente) { vm.consumirGuardado(); vm.limpiar(); alVolver() }
    }

    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp -> bmp?.let(vm::onFoto) }
    val permisoUbicacion = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        if (ok) vm.obtenerUbicacion() else vm.mostrarMensaje("Permiso de ubicación denegado. Actívalo en Ajustes para usar esta función.")
    }
    fun pedirUbicacion() {
        val concedido = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (concedido) vm.obtenerUbicacion() else permisoUbicacion.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(if (s.esEdicion) "Editar oportunidad" else "Nueva oportunidad") },
            navigationIcon = { IconButton(onClick = { vm.limpiar(); alVolver() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
        )
    }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            CampoValidado(s.nombre, vm::onNombre, "Nombre de la oportunidad", Icons.Filled.Title, s.nombreError)
            CampoSelector(s.tipo, Opciones.tipos, vm::onTipo, "Tipo de oportunidad", Icons.Filled.Category, s.tipoError)
            CampoValidado(s.descripcion, vm::onDescripcion, "Descripción", Icons.Filled.Description, s.descripcionError, unaLinea = false, lineasMin = 3)
            CampoValidado(s.fuente, vm::onFuente, "Fuente de información", Icons.Filled.Link, s.fuenteError)
            CampoSelector(s.prioridad, Opciones.prioridades, vm::onPrioridad, "Prioridad", Icons.Filled.Flag, s.prioridadError)
            CampoSelector(s.potencial, Opciones.potenciales, vm::onPotencial, "Potencial comercial", Icons.Filled.TrendingUp, s.potencialError)
            CampoSelector(s.estado, Opciones.estados, vm::onEstado, "Estado de gestión", Icons.Filled.Timeline, s.estadoError)
            CampoValidado(s.observaciones, vm::onObservaciones, "Observaciones (opcional)", Icons.Filled.Notes, null, unaLinea = false, lineasMin = 2)

            Text("Evidencia y referencia (opcional)", style = MaterialTheme.typography.titleSmall)
            OutlinedButton(onClick = { camara.launch(null) }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.PhotoCamera, null); Text("  Tomar foto de evidencia")
            }
            AnimatedVisibility(s.fotoPath != null) {
                AsyncImage(
                    model = s.fotoPath?.let { File(it) }, contentDescription = "Evidencia fotográfica", contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(180.dp).clip(MaterialTheme.shapes.medium)
                )
            }
            OutlinedButton(onClick = { pedirUbicacion() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.LocationOn, null); Text("  Obtener referencia geográfica")
            }
            AnimatedVisibility(s.latitud != null) {
                Text("📍 %.5f, %.5f".format(s.latitud ?: 0.0, s.longitud ?: 0.0), color = MaterialTheme.colorScheme.primary)
            }
            AnimatedVisibility(s.mensaje != null) { Text(s.mensaje ?: "", color = MaterialTheme.colorScheme.error) }

            val alpha by animateFloatAsState(if (s.guardando) 0.6f else 1f, label = "alphaGuardar")
            Button(onClick = vm::guardar, enabled = !s.guardando, modifier = Modifier.fillMaxWidth().alpha(alpha)) {
                if (s.guardando) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                else { Icon(Icons.Filled.Check, null); Text(if (s.esEdicion) "  Guardar cambios" else "  Registrar oportunidad") }
            }
        }
    }
}
