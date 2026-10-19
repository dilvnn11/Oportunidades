package cl.duoc.oportunidades.datos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oportunidades")
data class OportunidadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fuente: String,
    val prioridad: String,
    val potencial: String,
    val estado: String,
    val observaciones: String,
    val fotoPath: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val actualizadoEn: Long = System.currentTimeMillis()
)

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val correo: String,
    val nombre: String,
    val claveHash: String
)
