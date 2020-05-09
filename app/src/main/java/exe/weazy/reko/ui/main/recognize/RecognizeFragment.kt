package exe.weazy.reko.ui.main.recognize

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import exe.weazy.reko.R
import exe.weazy.reko.ui.camera.CameraActivity
import exe.weazy.reko.util.handleTopInsets
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

    private fun initListeners() {
        photoButton.setOnClickListener {
            openCamera()
        }

        chooseButton.setOnClickListener {
            Toast.makeText(requireContext(), "Choose", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }
}