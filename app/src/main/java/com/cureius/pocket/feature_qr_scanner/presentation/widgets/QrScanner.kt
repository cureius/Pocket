package com.cureius.pocket.feature_qr_scanner.presentation.widgets

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cureius.pocket.feature_transaction.presentation.util.Screen
import com.cureius.pocket.feature_upi_payment.presentation.manager.UpiPaymentViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrScanner(navController : NavHostController) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CameraPreview(navController = navController)
        }
    }
}


@Composable
fun CameraPreview(
    navController: NavHostController, upiPaymentViewModel: UpiPaymentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }

    AndroidView(factory = { AndroidViewContext ->
        PreviewView(AndroidViewContext).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }, modifier = Modifier.fillMaxSize(), update = { previewView ->
        val cameraSelector: CameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val barcodeAnalyser = QrCodeAnalyzer { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let { barcodeValue ->
                        barCodeVal.value = barcodeValue
                        val (upiId, payeeName, transactionAmount) = extractDetailsFromUPIString(
                            barcodeValue
                        )
                        val params = extractParamsFromUPIString(barcodeValue)
                        println(barcodeValue)
                        println(params)
                        navController.navigate(Screen.UpiPaymentScreen.route + "?upiId=${params["pa"].toString()}&receiverName=${params["pn"].toString()}&amount=${params["am"].toString()}&description=${params["tn"].toString()}")
                        Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
            }
        }, ContextCompat.getMainExecutor(context))
    })
}

fun extractParamsFromUPIString(upiString: String): Map<String, String> {
    val regex = """[?&]([^=]+)=([^&]+)""".toRegex()
    val matchResults = regex.findAll(upiString)

    val paramsMap = mutableMapOf<String, String>()

    for (matchResult in matchResults) {
        val (key, value) = matchResult.destructured
        paramsMap[key] = value
    }

    return paramsMap
}

fun extractDetailsFromUPIString(upiString: String): Triple<String?, String?, String?> {
    val regex = """pa=([^&]+).*?pn=([^&]+).*?am=([^&]+)""".toRegex()
    val matchResult = regex.find(upiString)

    var upiId: String? = null
    var payeeName: String? = null
    var transactionAmount: String? = null

    matchResult?.let {
        upiId = it.groupValues[1]
        payeeName = it.groupValues[2]
        transactionAmount = it.groupValues[3]
    }

    return Triple(upiId, payeeName, transactionAmount)
}