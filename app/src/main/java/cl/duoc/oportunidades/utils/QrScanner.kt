package cl.duoc.oportunidades.utils

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "QrScanner"

/**
 * Vista previa de la cámara (CameraX) que detecta códigos QR con ML Kit.
 * [onQrCodeScanned] se invoca una sola vez, en el hilo principal.
 */
@OptIn(ExperimentalGetImage::class)
@Composable
fun QrScanner(onQrCodeScanned: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    // Mantiene el callback actualizado sin reiniciar CameraX
    val currentCallback by rememberUpdatedState(onQrCodeScanned)

    // Una única vista previa
    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE // mejor compatibilidad con emuladores
        }
    }

    AndroidView(factory = { previewView }, modifier = modifier.fillMaxSize())

    DisposableEffect(lifecycleOwner, previewView) {
        val executor = Executors.newSingleThreadExecutor()
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        )
        val detectado = AtomicBoolean(false)
        var liberado = false
        var proveedor: ProcessCameraProvider? = null
        var analisis: ImageAnalysis? = null

        val futuro = ProcessCameraProvider.getInstance(context)
        futuro.addListener({
            if (liberado) return@addListener
            val provider = futuro.get().also { proveedor = it }

            val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also { analisis = it }

            analysis.setAnalyzer(executor) { imageProxy ->
                val media = imageProxy.image
                if (media == null || detectado.get()) {
                    imageProxy.close()
                    return@setAnalyzer
                }
                val entrada = InputImage.fromMediaImage(media, imageProxy.imageInfo.rotationDegrees)
                scanner.process(entrada)
                    .addOnSuccessListener { codigos ->
                        val valor = codigos.firstNotNullOfOrNull { it.rawValue }
                        if (valor != null && detectado.compareAndSet(false, true)) {
                            analysis.clearAnalyzer()
                            Log.d(TAG, "QR detectado: $valor")
                            ContextCompat.getMainExecutor(context).execute { currentCallback(valor) }
                        }
                    }
                    .addOnFailureListener { Log.e(TAG, "Error al procesar la imagen", it) }
                    .addOnCompleteListener { imageProxy.close() }
            }

            try {
                provider.unbindAll()
                provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
            } catch (e: Exception) {
                Log.e(TAG, "No se pudo enlazar la cámara", e)
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            liberado = true
            analisis?.clearAnalyzer()
            proveedor?.unbindAll()
            scanner.close()
            executor.shutdown()
            Log.d(TAG, "Recursos de cámara liberados")
        }
    }
}
