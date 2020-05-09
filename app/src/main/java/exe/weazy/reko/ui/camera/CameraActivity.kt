package exe.weazy.reko.ui.camera

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import com.google.common.util.concurrent.ListenableFuture
import exe.weazy.reko.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private val REQUEST_CAMERA_CODE = 101;
    private val REQUEST_WRITE_EXTERNAL_STORAGE_CODE = 102;

    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        rootViewCamera.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

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
            REQUEST_WRITE_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.all { it == PermissionChecker.PERMISSION_GRANTED }) {
                    //initCamera()
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
    }

    private fun initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
    }

    private fun showNoPermissions() {
        previewView.isVisible = false
        noPermissionLayout.isVisible = true
    }

    private fun hasPermissions() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PermissionChecker.PERMISSION_GRANTED
    }
}
