package exe.weazy.reko.ui.main.create

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import exe.weazy.reko.R
import kotlinx.android.synthetic.main.dialog_choose_meme.*

class ChooseMemeDialogFragment: DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choose_meme, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initListeners()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (dialog != null) {
            onDismiss(dialog)
        }
    }

    private fun initListeners() {
        fromGalleryButton.setOnClickListener {
            Toast.makeText(requireContext(), "From gallery", Toast.LENGTH_SHORT).show()
        }

        photoButton.setOnClickListener {
            Toast.makeText(requireContext(), "Photo", Toast.LENGTH_SHORT).show()
        }
    }
}