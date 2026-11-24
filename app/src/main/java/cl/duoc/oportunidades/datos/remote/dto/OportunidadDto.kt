package cl.duoc.oportunidades.datos.remote.dto

import cl.duoc.oportunidades.datos.local.OportunidadEntity

data class OportunidadDto(
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuente: String,
    val prioridad: String,
    val potencial: String,
    val estado: String,
    val observaciones: String = "",
    val fotoPath: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val actualizadoEn: Long? = null
) {
    fun toEntity(): OportunidadEntity = OportunidadEntity(
        id = id,
        nombre = nombre,
        tipo = tipo,
        descripcion = descripcion,
        fuente = fuente,
        prioridad = prioridad,
        potencial = potencial,
        estado = estado,
        observaciones = observaciones,
        fotoPath = fotoPath,
        latitud = latitud,
        longitud = longitud,
        actualizadoEn = actualizadoEn ?: System.currentTimeMillis()
    )

    companion object {
        fun fromEntity(e: OportunidadEntity): OportunidadDto = OportunidadDto(
            id = e.id,
            nombre = e.nombre,
            tipo = e.tipo,
            descripcion = e.descripcion,
            fuente = e.fuente,
            prioridad = e.prioridad,
            potencial = e.potencial,
            estado = e.estado,
            observaciones = e.observaciones,
            fotoPath = e.fotoPath,
            latitud = e.latitud,
            longitud = e.longitud,
            actualizadoEn = e.actualizadoEn
        )
    }
}
