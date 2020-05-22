package exe.weazy.reko.ui.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.core.view.isVisible
import exe.weazy.reko.R
import exe.weazy.reko.ui.InsetsHelper
import exe.weazy.reko.ui.image.ImageActivity
import exe.weazy.reko.util.values.IMAGE_PATH
import exe.weazy.reko.util.values.REQUEST_CAMERA_CODE
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.*

class CameraActivity : AppCompatActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture
    private var isBackCamera = true

    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        rootViewCamera.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        InsetsHelper.handleBottom(true, photoButton)

        init()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    initCamera()
                } else {
                    showNoPermissions()
                }
            }
            else -> {
                showNoPermissions()
            }
        }
    }

    private fun init() {
        if (hasPermissions()) {
            initCamera()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CAMERA_CODE)
        }

        initListeners()
    }

    private fun initListeners() {
        photoButton.setOnClickListener {
            val filename = "${Date().time.toString(16)}.jpeg"
            val file = File(externalMediaDirs.first(), filename)
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            imageCapture.takePicture(outputFileOptions, { it.run() },
                object: ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        openImage(file.toUri())
                    }

                    override fun onError(exception: ImageCaptureException) {
                        runOnUiThread {
                            Toast.makeText(this@CameraActivity, "Ошибка при сохранении изображения", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isBackCamera) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
            .build()

        imageCapture = ImageCapture.Builder()
            .setTargetResolution(Size(720, 1280))
            .setTargetRotation(previewView.display.rotation)
            .build()

        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
    }

    private fun showNoPermissions() {
        previewView.isVisible = false
        noPermissionLayout.isVisible = true
    }

    private fun openImage(path: Uri) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(IMAGE_PATH, path)
        startActivity(intent)
        finish()
    }

    private fun hasPermissions() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PermissionChecker.PERMISSION_GRANTED
    }
}
