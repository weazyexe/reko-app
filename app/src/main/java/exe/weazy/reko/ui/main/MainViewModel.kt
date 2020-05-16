package exe.weazy.reko.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.RecognizedRepository
import exe.weazy.reko.data.SettingsRepository
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.subscribe
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var recognizedRepository: RecognizedRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    val state = MutableLiveData(ScreenState.DEFAULT)
    val recognized = MutableLiveData(listOf<Recognized>())

    var errorMessage: String? = null

    init {
        App.getComponent().inject(this)
    }

    fun fetch() {
        state.postValue(ScreenState.LOADING)
        subscribe(recognizedRepository.fetch(), {
            if (it.isEmpty()) {
                state.postValue(ScreenState.EMPTY)
            } else {
                recognized.postValue(it)
                state.postValue(ScreenState.SUCCESS)
            }
        }, {
            errorMessage = it.message
            state.postValue(ScreenState.ERROR)
        })
    }

    fun getRecognizer(): RecognizerName = settingsRepository.getRecognizer()

    fun saveRecognizer(isSkyBiometryRecognizer: Boolean) {
        if (isSkyBiometryRecognizer) {
            settingsRepository.saveRecognizer(RecognizerName.SKY_BIOMETRY)
        } else {
            settingsRepository.saveRecognizer(RecognizerName.LOCAL)
        }
    }
}