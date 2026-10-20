package cl.duoc.oportunidades.ui.oportunidad

/**
 * Estado inmutable de la pantalla OportunidadFormScreen.
 * Lo expone OportunidadViewModel mediante StateFlow; la UI solo lo lee y dibuja.
 * Los campos *Error los calcula el ViewModel con dominio/Validadores.
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
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion get() = id != 0L
}
