package exe.weazy.reko.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.leinardi.android.speeddial.SpeedDialView
import exe.weazy.reko.R
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.recycler.RecognizedAdapter
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.ui.camera.CameraActivity
import exe.weazy.reko.ui.image.ImageActivity
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.getDefaultColor
import exe.weazy.reko.util.handleBottomInsets
import exe.weazy.reko.util.handleTopInsets
import exe.weazy.reko.util.values.IMAGE_PATH
import exe.weazy.reko.util.values.RECOGNIZED_KEY
import exe.weazy.reko.util.values.REQUEST_GALLERY_CODE
import exe.weazy.reko.util.values.REQUEST_READ_EXTERNAL_STORAGE_CODE
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: RecognizedAdapter

    private var isSkyBiometryRecognizer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootViewMain.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        speedDialView.inflate(R.menu.main_menu)
        handleBottomInsets(speedDialView)
        handleTopInsets(recognizersBar)

        viewModel = useViewModel(this, MainViewModel::class.java)
        viewModel.fetch()

        isSkyBiometryRecognizer = viewModel.getRecognizer() == RecognizerName.SKY_BIOMETRY
        changeRecognizer()

        initObservers()
        initListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_GALLERY_CODE -> {
                if (data != null && data.data != null) {
                    handleContent(data.data)
                }
            }
            else -> {
                Toast.makeText(this, "Wow", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, R.string.no_required_permissions, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.recognized.observe(this, Observer {
            showRecognized(it)
        })
    }

    private fun initListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetch()
            swipeRefreshLayout.isRefreshing = false
        }

        localCardView.setOnClickListener {
            isSkyBiometryRecognizer = false
            changeRecognizer()
            viewModel.saveRecognizer(isSkyBiometryRecognizer)
        }

        skyBiometryCardView.setOnClickListener {
            isSkyBiometryRecognizer = true
            changeRecognizer()
            viewModel.saveRecognizer(isSkyBiometryRecognizer)
        }

        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fromGalleryButton -> {
                    pickAnImage()
                    speedDialView.close()
                    return@OnActionSelectedListener true
                }
                R.id.photoButton -> {
                    openCamera()
                    speedDialView.close()
                    return@OnActionSelectedListener true
                }
            }
            false
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.ERROR -> {
                errorLayout.isVisible = true
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
            ScreenState.SUCCESS -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = true
                emptyLayout.isVisible = false
            }
            ScreenState.EMPTY -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = true

                errorLayout.text = getString(R.string.loading_error) + "\n\n${viewModel.errorMessage ?: ""}"
            }
            ScreenState.LOADING -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = true
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
            ScreenState.DEFAULT -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                recognizedRecyclerView.isVisible = false
                emptyLayout.isVisible = false
            }
        }
    }

    private fun showRecognized(recognized: List<Recognized>) {
        if (::adapter.isInitialized) {
            adapter.update(recognized)
        } else {
            adapter = RecognizedAdapter(recognized.toMutableList()) {
                openImage(it)
            }

            recognizedRecyclerView.adapter = adapter
            recognizedRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun changeRecognizer() {
        if (isSkyBiometryRecognizer) {
            localCardView.setCardBackgroundColor(whiteColor())
            localTextView.setTextColor(primaryColor())
            localIconImageView.setColorFilter(primaryColor())

            skyBiometryCardView.setCardBackgroundColor(primaryColor())
            skyBiometryTextView.setTextColor(whiteColor())
            skyBiometryIconImageView.setColorFilter(whiteColor())
        } else {
            localCardView.setCardBackgroundColor(primaryColor())
            localTextView.setTextColor(whiteColor())
            localIconImageView.setColorFilter(whiteColor())

            skyBiometryCardView.setCardBackgroundColor(whiteColor())
            skyBiometryTextView.setTextColor(primaryColor())
            skyBiometryIconImageView.setColorFilter(primaryColor())
        }
    }

    private fun primaryColor() = getDefaultColor(this, R.attr.colorAccent)

    private fun whiteColor() = ContextCompat.getColor(this, R.color.colorWhite)

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun openImage(uri: Uri) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(IMAGE_PATH, uri)
        startActivity(intent)
    }

    private fun openImage(recognized: Recognized) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(RECOGNIZED_KEY, recognized)
        startActivity(intent)
    }

    private fun handleContent(content: Uri?) {
        // IT'S MAGIC 🌈
        if (content != null) {
            try {
                val inputStream = contentResolver?.openInputStream(content)
                val bytes = inputStream?.readBytes()
                val file = File(filesDir, content.lastPathSegment ?: Date().time.toString())
                file.writeBytes(bytes!!)
                openImage(file.toUri())
            } catch (e: Exception) {
                Toast.makeText(this, e.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
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
            = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
}
