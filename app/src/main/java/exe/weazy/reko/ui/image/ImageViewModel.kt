package exe.weazy.reko.ui.image

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.RecognizedRepository
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.recognizer.Recognizer
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.subscribe
import javax.inject.Inject

class ImageViewModel: ViewModel() {

    @Inject
    lateinit var recognizer: Recognizer

    @Inject
    lateinit var recognizedRepository: RecognizedRepository

    val state = MutableLiveData(ScreenState.DEFAULT)
    val recognized = MutableLiveData<Recognized>()

    init {
        App.getComponent().inject(this)
    }

    fun recognize(imageUri: Uri) {
        state.postValue(ScreenState.LOADING)

        val observable = recognizer.recognize(imageUri)
            .flatMap { recognizedRepository.save(it) }

        subscribe(observable, {
            recognized.postValue(it)
            state.postValue(ScreenState.SUCCESS)
        }, {
            state.postValue(ScreenState.ERROR)
        })
    }
}