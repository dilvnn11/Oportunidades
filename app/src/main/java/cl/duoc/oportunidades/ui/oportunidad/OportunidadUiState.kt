package cl.duoc.oportunidades.ui.oportunidad

import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse

/**
 * Estado inmutable de las pantallas del módulo de Oportunidades.
 */
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
    val guardadoExitosamente: Boolean = false,
    val indicadores: MindicadorResponse? = null,
    val cargandoIndicadores: Boolean = false
) {
    val esEdicion get() = id != 0L
}
