package cl.duoc.oportunidades.ui.oportunidad

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.oportunidades.datos.local.AppDatabase
import cl.duoc.oportunidades.datos.local.OportunidadEntity
import cl.duoc.oportunidades.datos.repository.OportunidadRepository
import cl.duoc.oportunidades.dominio.Validadores
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

data class OportunidadUiState(
    val id: Long = 0,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val fuente: String = "",
    val prioridad: String = "",
    val potencial: String = "",
    val estado: String = "Evento identificado",
    val observaciones: String = "",
    val fotoPath: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val fuenteError: String? = null,
    val prioridadError: String? = null,
    val potencialError: String? = null,
    val estadoError: String? = null,
    val mensaje: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion get() = id != 0L
}

class OportunidadViewModel(private val app: Application) : AndroidViewModel(app) {
    private val repo = OportunidadRepository(AppDatabase.obtener(app).oportunidadDao())

    /** Lista reactiva: se actualiza sola cuando Room recibe un cambio. */
    val oportunidades: StateFlow<List<OportunidadEntity>> = repo.observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(OportunidadUiState())
    val uiState: StateFlow<OportunidadUiState> = _uiState.asStateFlow()

    // ---- eventos de formulario (validación en cada cambio) ----
    fun onNombre(v: String) = _uiState.update { it.copy(nombre = v, nombreError = Validadores.nombre(v)) }
    fun onTipo(v: String) = _uiState.update { it.copy(tipo = v, tipoError = Validadores.tipo(v)) }
    fun onDescripcion(v: String) = _uiState.update { it.copy(descripcion = v, descripcionError = Validadores.descripcion(v)) }
    fun onFuente(v: String) = _uiState.update { it.copy(fuente = v, fuenteError = Validadores.fuente(v)) }
    fun onPrioridad(v: String) = _uiState.update { it.copy(prioridad = v, prioridadError = Validadores.prioridad(v)) }
    fun onPotencial(v: String) = _uiState.update { it.copy(potencial = v, potencialError = Validadores.potencial(v)) }
    fun onEstado(v: String) = _uiState.update { it.copy(estado = v, estadoError = Validadores.estado(v)) }
    fun onObservaciones(v: String) = _uiState.update { it.copy(observaciones = v) }
    fun mostrarMensaje(m: String?) = _uiState.update { it.copy(mensaje = m) }

    /** id = 0 → formulario nuevo; id > 0 → carga la oportunidad desde Room para editarla. */
    fun cargar(id: Long) {
        if (id == 0L) { _uiState.value = OportunidadUiState(); return }
        viewModelScope.launch {
            repo.obtener(id)?.let { o ->
                _uiState.value = OportunidadUiState(
                    id = o.id, nombre = o.nombre, tipo = o.tipo, descripcion = o.descripcion,
                    fuente = o.fuente, prioridad = o.prioridad, potencial = o.potencial,
                    estado = o.estado, observaciones = o.observaciones,
                    fotoPath = o.fotoPath, latitud = o.latitud, longitud = o.longitud
                )
            }
        }
    }

    fun limpiar() { _uiState.value = OportunidadUiState() }
    fun consumirGuardado() = _uiState.update { it.copy(guardadoExitosamente = false) }

    fun guardar() {
        val s = _uiState.value
        val nuevo = s.copy(
            nombreError = Validadores.nombre(s.nombre), tipoError = Validadores.tipo(s.tipo),
            descripcionError = Validadores.descripcion(s.descripcion), fuenteError = Validadores.fuente(s.fuente),
            prioridadError = Validadores.prioridad(s.prioridad), potencialError = Validadores.potencial(s.potencial),
            estadoError = Validadores.estado(s.estado)
        )
        _uiState.value = nuevo
        val hayErrores = listOf(
            nuevo.nombreError, nuevo.tipoError, nuevo.descripcionError, nuevo.fuenteError,
            nuevo.prioridadError, nuevo.potencialError, nuevo.estadoError
        ).any { it != null }
        if (hayErrores) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            repo.guardar(
                OportunidadEntity(
                    id = s.id, nombre = s.nombre.trim(), tipo = s.tipo, descripcion = s.descripcion.trim(),
                    fuente = s.fuente.trim(), prioridad = s.prioridad, potencial = s.potencial,
                    estado = s.estado, observaciones = s.observaciones.trim(),
                    fotoPath = s.fotoPath, latitud = s.latitud, longitud = s.longitud
                )
            )
            _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
        }
    }

    // ---- Recurso nativo 1: cámara (foto de evidencia) ----
    fun onFoto(bitmap: Bitmap) {
        viewModelScope.launch {
            val ruta = withContext(Dispatchers.IO) {
                val f = File(app.filesDir, "evidencia_${System.currentTimeMillis()}.jpg")
                f.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
                f.absolutePath
            }
            _uiState.update { it.copy(fotoPath = ruta, mensaje = null) }
        }
    }

    // ---- Recurso nativo 2: ubicación (el permiso se solicita en la UI) ----
    @SuppressLint("MissingPermission")
    fun obtenerUbicacion() {
        try {
            val lm = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val loc = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
                .filter { lm.isProviderEnabled(it) }
                .mapNotNull { lm.getLastKnownLocation(it) }
                .maxByOrNull { it.time }
            if (loc != null) _uiState.update { it.copy(latitud = loc.latitude, longitud = loc.longitude, mensaje = null) }
            else _uiState.update { it.copy(mensaje = "No se pudo obtener la ubicación. Activa el GPS e inténtalo nuevamente.") }
        } catch (e: SecurityException) {
            _uiState.update { it.copy(mensaje = "Permiso de ubicación denegado") }
        }
    }
}
