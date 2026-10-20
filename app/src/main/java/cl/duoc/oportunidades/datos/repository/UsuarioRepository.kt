package cl.duoc.oportunidades.datos.repository

import cl.duoc.oportunidades.datos.local.UsuarioDao
import cl.duoc.oportunidades.datos.local.UsuarioEntity
import java.security.MessageDigest

class UsuarioRepository(private val dao: UsuarioDao) {
    private fun hash(s: String) =
        MessageDigest.getInstance("SHA-256").digest(s.toByteArray()).joinToString("") { "%02x".format(it) }

    /** @return false si el correo ya estaba registrado. */
    suspend fun registrar(nombre: String, correo: String, clave: String): UsuarioEntity? {
        val c = correo.trim().lowercase()
        if (dao.buscar(c) != null) return null
        val u = UsuarioEntity(c, nombre.trim(), hash(clave))
        dao.insertar(u)
        return u
    }

    suspend fun iniciarSesion(correo: String, clave: String): UsuarioEntity? =
        dao.buscar(correo.trim().lowercase())?.takeIf { it.claveHash == hash(clave) }
}
