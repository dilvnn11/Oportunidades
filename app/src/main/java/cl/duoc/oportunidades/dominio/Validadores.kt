package cl.duoc.oportunidades.dominio

/** Reglas de validación puras (sin Android ni Compose). Devuelven el mensaje de error o null. */
object Validadores {
    private val CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // ---- Oportunidad ----
    fun nombre(v: String) = when {
        v.isBlank() -> "El nombre de la oportunidad es obligatorio"
        v.trim().length < 3 -> "Mínimo 3 caracteres"
        else -> null
    }
    fun tipo(v: String) = when {
        v.isBlank() -> "El tipo de oportunidad es obligatorio"
        v !in Opciones.tipos -> "Selecciona un tipo válido"
        else -> null
    }
    fun descripcion(v: String) = when {
        v.isBlank() -> "La descripción es obligatoria"
        v.trim().length < 10 -> "Describe la oportunidad (mínimo 10 caracteres)"
        else -> null
    }
    fun fuente(v: String) = if (v.isBlank()) "La fuente de información es obligatoria" else null
    fun prioridad(v: String) = when {
        v.isBlank() -> "La prioridad es obligatoria"
        v !in Opciones.prioridades -> "Selecciona una prioridad válida"
        else -> null
    }
    fun potencial(v: String) = when {
        v.isBlank() -> "El potencial comercial es obligatorio"
        v !in Opciones.potenciales -> "Selecciona una opción válida"
        else -> null
    }
    fun estado(v: String) = when {
        v.isBlank() -> "El estado de gestión es obligatorio"
        v !in Opciones.estados -> "Selecciona un estado válido"
        else -> null
    }

    // ---- Autenticación ----
    fun nombrePersona(v: String) = when {
        v.isBlank() -> "El nombre es obligatorio"
        v.trim().length < 3 -> "Mínimo 3 caracteres"
        !v.all { it.isLetter() || it == ' ' } -> "Solo letras y espacios"
        else -> null
    }
    fun correo(v: String) = when {
        v.isBlank() -> "El correo es obligatorio"
        !CORREO.matches(v.trim()) -> "Formato de correo inválido"
        else -> null
    }
    fun clave(v: String) = when {
        v.isEmpty() -> "La contraseña es obligatoria"
        v.length < 8 -> "Mínimo 8 caracteres"
        !v.any { it.isDigit() } -> "Debe incluir al menos un número"
        !v.any { it.isUpperCase() } -> "Debe incluir una mayúscula"
        else -> null
    }
    fun confirmacion(clave: String, confirm: String) = when {
        confirm.isEmpty() -> "Confirma tu contraseña"
        clave != confirm -> "Las contraseñas no coinciden"
        else -> null
    }
}
