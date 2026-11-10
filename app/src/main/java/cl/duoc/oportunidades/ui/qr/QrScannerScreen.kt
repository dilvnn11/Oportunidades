package cl.duoc.oportunidades.ui.qr

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.livedata.observeAsState
import cl.duoc.oportunidades.utils.CameraPermissionHelper
import cl.duoc.oportunidades.utils.QrScanner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(viewModel: QrViewModel, alVolver: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val qrResult by viewModel.qrResult.observeAsState()
    val historial by viewModel.historial.observeAsState(emptyList())
    var isScanning by remember { mutableStateOf(true) }
    var hasCameraPermission by remember { mutableStateOf(CameraPermissionHelper.hasCameraPermission(context)) }

    val permisoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        hasCameraPermission = ok
        Toast.makeText(
            context,
            if (ok) "Permiso de cámara concedido" else "Se necesita permiso de cámara para escanear QR",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Revisa el permiso al volver al primer plano (por si cambió desde Ajustes)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) hasCameraPermission = CameraPermissionHelper.hasCameraPermission(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Lector QR") },
            navigationIcon = { IconButton(onClick = alVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
        )
    }) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                // 1) Sin permiso
                !hasCameraPermission -> {
                    Text("Permiso de cámara requerido", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(16.dp))
                    Text("Para escanear códigos QR, necesitamos acceso a la cámara", textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { permisoLauncher.launch(Manifest.permission.CAMERA) }) { Text("Conceder permiso de cámara") }
                }
                // 2) Escaneando
                qrResult == null && isScanning -> {
                    Text("Escanea un código QR", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) {
                        QrScanner(onQrCodeScanned = { contenido ->
                            viewModel.onQrDetected(contenido)
                            isScanning = false
                            Toast.makeText(context, "QR detectado!", Toast.LENGTH_SHORT).show()
                        })
                        Surface(
                            modifier = Modifier.size(250.dp).border(3.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
                            color = Color.Transparent
                        ) {}
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Enfoca el código QR en el marco central", style = MaterialTheme.typography.bodyMedium)
                    Text("La cámara debería activarse automáticamente", style = MaterialTheme.typography.bodySmall)
                }
                // 3) Resultado + historial guardado en Room
                else -> {
                    Text("QR Detectado:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Text(qrResult?.content.orEmpty(), Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.clearResult(); isScanning = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Escanear otro código QR")
                    }
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.fillMaxWidth()) {
                        Text("Historial guardado (Room)", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.CenterStart))
                        if (historial.isNotEmpty()) IconButton(onClick = viewModel::borrarHistorial, modifier = Modifier.align(Alignment.CenterEnd)) {
                            Icon(Icons.Filled.DeleteSweep, "Borrar historial")
                        }
                    }
                    LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(historial, key = { it.id }) { qr ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(qr.contenido, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(qr.escaneadoEn)),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
