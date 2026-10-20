package cl.duoc.oportunidades.ui.auth

/** Estado de las pantallas de inicio de sesión y registro (lo expone AuthViewModel). */
data class AuthUiState(
    val nombre: String = "", val correo: String = "", val clave: String = "", val confirmacion: String = "",
    val nombreError: String? = null, val correoError: String? = null,
    val claveError: String? = null, val confirmacionError: String? = null,
    val mensaje: String? = null, val cargando: Boolean = false
)
