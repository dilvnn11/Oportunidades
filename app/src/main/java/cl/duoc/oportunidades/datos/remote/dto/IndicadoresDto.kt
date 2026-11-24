package cl.duoc.oportunidades.datos.remote.dto

data class IndicadorDetalle(
    val codigo: String,
    val nombre: String,
    val unidad_medida: String,
    val fecha: String,
    val valor: Double
)

data class MindicadorResponse(
    val version: String? = null,
    val autor: String? = null,
    val fecha: String? = null,
    val uf: IndicadorDetalle? = null,
    val dolar: IndicadorDetalle? = null,
    val euro: IndicadorDetalle? = null,
    val utm: IndicadorDetalle? = null
)
