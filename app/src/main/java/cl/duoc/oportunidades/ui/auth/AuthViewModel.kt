package cl.duoc.oportunidades.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.oportunidades.datos.local.AppDatabase
import cl.duoc.oportunidades.datos.local.UsuarioEntity
import cl.duoc.oportunidades.datos.repository.UsuarioRepository
import cl.duoc.oportunidades.dominio.Validadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val nombre: String = "", val correo: String = "", val clave: String = "", val confirmacion: String = "",
    val nombreError: String? = null, val correoError: String? = null,
    val claveError: String? = null, val confirmacionError: String? = null,
    val mensaje: String? = null, val cargando: Boolean = false
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = UsuarioRepository(AppDatabase.obtener(app).usuarioDao())

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _usuario = MutableStateFlow<UsuarioEntity?>(null)
    val usuario: StateFlow<UsuarioEntity?> = _usuario.asStateFlow()

    fun onNombre(v: String) = _uiState.update { it.copy(nombre = v, nombreError = Validadores.nombrePersona(v), mensaje = null) }
    fun onCorreo(v: String) = _uiState.update { it.copy(correo = v, correoError = Validadores.correo(v), mensaje = null) }
    fun onClave(v: String) = _uiState.update {
        it.copy(
            clave = v, claveError = Validadores.clave(v), mensaje = null,
            confirmacionError = if (it.confirmacion.isEmpty()) null else Validadores.confirmacion(v, it.confirmacion)
        )
    }
    fun onConfirmacion(v: String) = _uiState.update { it.copy(confirmacion = v, confirmacionError = Validadores.confirmacion(it.clave, v), mensaje = null) }
    fun limpiar() { _uiState.value = AuthUiState() }
    fun cerrarSesion() { _usuario.value = null; limpiar() }

    fun iniciarSesion(onExito: () -> Unit) {
        val s = _uiState.value
        val cErr = Validadores.correo(s.correo)
        val pErr = if (s.clave.isEmpty()) "La contraseña es obligatoria" else null
        _uiState.update { it.copy(correoError = cErr, claveError = pErr) }
        if (cErr != null || pErr != null) return
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val u = repo.iniciarSesion(s.correo, s.clave)
            if (u != null) { _usuario.value = u; limpiar(); onExito() }
            else _uiState.update { it.copy(cargando = false, mensaje = "Correo o contraseña incorrectos") }
        }
    }

    fun registrar(onExito: () -> Unit) {
        val s = _uiState.value
        val e = listOf(
            Validadores.nombrePersona(s.nombre), Validadores.correo(s.correo),
            Validadores.clave(s.clave), Validadores.confirmacion(s.clave, s.confirmacion)
        )
        _uiState.update { it.copy(nombreError = e[0], correoError = e[1], claveError = e[2], confirmacionError = e[3]) }
        if (e.any { it != null }) return
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val u = repo.registrar(s.nombre, s.correo, s.clave)
            if (u == null) _uiState.update { it.copy(cargando = false, correoError = "Este correo ya está registrado") }
            else { _usuario.value = u; limpiar(); onExito() }
        }
    }
}
