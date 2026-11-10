package cl.duoc.oportunidades.datos.repository

import cl.duoc.oportunidades.datos.local.QrDao
import cl.duoc.oportunidades.datos.local.QrEntity
import cl.duoc.oportunidades.datos.model.QrResult
import kotlinx.coroutines.flow.Flow

class QrRepository(private val dao: QrDao) {
    /** Procesa el texto leído por el escáner y lo convierte en un QrResult. */
    fun processQrContent(content: String): QrResult = QrResult(content.trim())

    /** Guarda el escaneo en Room. */
    suspend fun guardar(result: QrResult) {
        if (result.content.isNotEmpty()) dao.insertar(QrEntity(contenido = result.content))
    }

    fun observarHistorial(): Flow<List<QrEntity>> = dao.observarTodos()

    suspend fun borrarHistorial() = dao.borrarTodos()
}
