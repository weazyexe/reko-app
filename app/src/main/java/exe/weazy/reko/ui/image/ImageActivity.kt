package exe.weazy.reko.ui.image

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.toTitleCase
import exe.weazy.reko.util.extensions.useViewModel
import exe.weazy.reko.util.values.IMAGE_PATH
import kotlinx.android.synthetic.main.activity_image.*
import org.ocpsoft.prettytime.PrettyTime

class ImageActivity : AppCompatActivity() {

    private lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        viewModel = useViewModel(this, ImageViewModel::class.java)

        val path = intent.getParcelableExtra<Uri>(IMAGE_PATH)
        if (path != null) {
            showImage(path)
            viewModel.recognize(path)
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.state.observe(this, Observer {
            setState(it)
        })

        viewModel.recognized.observe(this, Observer {
            showRecognized(it)
        })
    }

    private fun showImage(uri: Uri) {
        Glide
            .with(this)
            .load(uri)
            .centerCrop()
            .into(imageView)
    }

    private fun setState(state: ScreenState) {
        when (state) {
            ScreenState.ERROR -> {
                errorLayout.isVisible = true
                progressLayout.isVisible = false
                infoLayout.isVisible = false
            }
            ScreenState.SUCCESS -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                infoLayout.isVisible = true
            }
            ScreenState.LOADING -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = true
                infoLayout.isVisible = false
            }
            ScreenState.DEFAULT, ScreenState.EMPTY  -> {
                errorLayout.isVisible = false
                progressLayout.isVisible = false
                infoLayout.isVisible = false
            }
        }
    }

    private fun showRecognized(recognized: Recognized) {
        dateTextView.text = PrettyTime().format(recognized.date)

        val mainEmotion = recognized.emotions.maxBy { it.value }?.key
        if (mainEmotion != null) {
            mainEmotionTextView.text = mainEmotion
            otherEmotionsTextView.text = composeOtherEmotions(recognized.emotions, mainEmotion)
        }
    }

    private fun composeOtherEmotions(emotions: Map<String, Int>, main: String): String {
        val sb = StringBuilder()

        emotions.forEach {
            if (it.key != main) {
                sb.append("${it.key.toTitleCase()} - ${it.value}%\n")
            }
        }

        return sb.toString()
    }
}
