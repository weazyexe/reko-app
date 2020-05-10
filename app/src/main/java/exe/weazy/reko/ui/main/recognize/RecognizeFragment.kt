package exe.weazy.reko.ui.main.recognize

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import exe.weazy.reko.R
import exe.weazy.reko.ui.camera.CameraActivity
import exe.weazy.reko.ui.image.ImageActivity
import exe.weazy.reko.util.handleTopInsets
import exe.weazy.reko.util.values.IMAGE_PATH
import exe.weazy.reko.util.values.REQUEST_GALLERY_CODE
import exe.weazy.reko.util.values.REQUEST_READ_EXTERNAL_STORAGE_CODE
import kotlinx.android.synthetic.main.fragment_recognize.*


class RecognizeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recognize, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleTopInsets(profileToolbarLayout)
        initListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_GALLERY_CODE -> {
                if (data != null && data.data != null) {
                    val content = data.data
                    if (content != null) {
                        openImage(content)
                    }
                }
            }
            else -> {

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    openImagePicker()
                }
            }
            else -> {
                Toast.makeText(requireContext(), R.string.no_required_permissions, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initListeners() {
        photoButton.setOnClickListener {
            openCamera()
        }

        chooseButton.setOnClickListener {
            pickAnImage()
        }
    }

    private fun openCamera() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun openImage(uri: Uri) {
        val intent = Intent(requireContext(), ImageActivity::class.java)
        intent.putExtra(IMAGE_PATH, uri)
        startActivity(intent)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun pickAnImage() {
        if (hasStoragePermission()) {
            openImagePicker()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE_CODE)
        }
    }

    private fun hasStoragePermission()
            = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
}