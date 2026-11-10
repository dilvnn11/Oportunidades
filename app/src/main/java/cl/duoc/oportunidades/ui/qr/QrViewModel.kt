package cl.duoc.oportunidades.ui.qr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cl.duoc.oportunidades.datos.local.AppDatabase
import cl.duoc.oportunidades.datos.local.QrEntity
import cl.duoc.oportunidades.datos.model.QrResult
import cl.duoc.oportunidades.datos.repository.QrRepository
import kotlinx.coroutines.launch

/** MVVM: expone el resultado del escaneo con LiveData y el historial guardado en Room. */
class QrViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = QrRepository(AppDatabase.obtener(app).qrDao())

    private val _qrResult = MutableLiveData<QrResult?>()
    val qrResult: LiveData<QrResult?> = _qrResult

    val historial: LiveData<List<QrEntity>> = repository.observarHistorial().asLiveData()

    fun onQrDetected(content: String) {
        val resultado = repository.processQrContent(content)
        _qrResult.value = resultado
        viewModelScope.launch { repository.guardar(resultado) }
    }

    /** Deja el estado en null para permitir un nuevo escaneo. */
    fun clearResult() {
        _qrResult.value = null
    }

    fun borrarHistorial() {
        viewModelScope.launch { repository.borrarHistorial() }
    }
}
