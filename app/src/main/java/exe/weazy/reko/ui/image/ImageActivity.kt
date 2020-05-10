package exe.weazy.reko.ui.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.util.values.IMAGE_PATH
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val path = intent.getStringExtra(IMAGE_PATH)
        if (path != null) {
            showImage(path)
        }
    }

    private fun showImage(path: String) {
        Glide
            .with(this)
            .load(File(path))
            .centerCrop()
            .into(imageView)
    }
}
