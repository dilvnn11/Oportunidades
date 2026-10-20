package cl.duoc.oportunidades.datos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Usuario registrado. La contraseña se guarda solo como hash SHA-256 (ver UsuarioRepository). */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val correo: String,
    val nombre: String,
    val claveHash: String
)
