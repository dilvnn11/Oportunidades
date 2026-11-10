package cl.duoc.oportunidades.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.oportunidades.ui.componentes.CampoValidado

@Composable
private fun AuthLayout(titulo: String, subtitulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible, enter = fadeIn() + slideInVertically { -it / 2 }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Business, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.primary)
                Text(titulo, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                Text(subtitulo, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { contenido() }
    }
}

@Composable
fun LoginScreen(vm: AuthViewModel, alIngresar: () -> Unit, alRegistrarse: () -> Unit) {
    val s by vm.uiState.collectAsStateWithLifecycle()
    AuthLayout("Gestión de Oportunidades", "Activaciones corporativas") {
        CampoValidado(s.correo, vm::onCorreo, "Correo", Icons.Filled.Email, s.correoError, teclado = KeyboardType.Email)
        CampoValidado(s.clave, vm::onClave, "Contraseña", Icons.Filled.Lock, s.claveError, esClave = true)
        AnimatedVisibility(s.mensaje != null) { Text(s.mensaje ?: "", color = MaterialTheme.colorScheme.error) }
        Button(onClick = { vm.iniciarSesion(alIngresar) }, enabled = !s.cargando, modifier = Modifier.fillMaxWidth()) {
            if (s.cargando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) else Text("Iniciar sesión")
        }
        TextButton(onClick = { vm.limpiar(); alRegistrarse() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

@Composable
fun RegistroScreen(vm: AuthViewModel, alRegistrar: () -> Unit, alVolver: () -> Unit) {
    val s by vm.uiState.collectAsStateWithLifecycle()
    AuthLayout("Crear cuenta", "Registra tu usuario corporativo") {
        CampoValidado(s.nombre, vm::onNombre, "Nombre completo", Icons.Filled.Person, s.nombreError)
        CampoValidado(s.correo, vm::onCorreo, "Correo", Icons.Filled.Email, s.correoError, teclado = KeyboardType.Email)
        CampoValidado(s.clave, vm::onClave, "Contraseña", Icons.Filled.Lock, s.claveError, esClave = true)
        CampoValidado(s.confirmacion, vm::onConfirmacion, "Confirmar contraseña", Icons.Filled.Lock, s.confirmacionError, esClave = true)
        Button(onClick = { vm.registrar(alRegistrar) }, enabled = !s.cargando, modifier = Modifier.fillMaxWidth()) {
            if (s.cargando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) else Text("Registrarme")
        }
        TextButton(onClick = { vm.limpiar(); alVolver() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Ya tengo cuenta")
        }
    }
}
