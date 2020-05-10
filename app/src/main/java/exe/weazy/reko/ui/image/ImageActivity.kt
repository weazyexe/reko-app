package exe.weazy.reko.ui.image

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.util.values.IMAGE_PATH
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val path = intent.getParcelableExtra<Uri>(IMAGE_PATH)
        if (path != null) {
            showImage(path)
        }
    }

    private fun showImage(uri: Uri) {
        Glide
            .with(this)
            .load(uri)
            .centerCrop()
            .into(imageView)
    }
}
