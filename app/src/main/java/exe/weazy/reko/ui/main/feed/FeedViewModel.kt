package exe.weazy.reko.ui.main.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.RecognizedRepository
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.subscribe
import javax.inject.Inject

class FeedViewModel : ViewModel() {

    @Inject
    lateinit var repository: RecognizedRepository

    val state = MutableLiveData(ScreenState.DEFAULT)
    val recognized = MutableLiveData(listOf<Recognized>())

    init {
        App.getComponent().inject(this)
    }

    fun fetch() {
        state.postValue(ScreenState.LOADING)
        subscribe(repository.fetch(), {
            if (it.isEmpty()) {
                state.postValue(ScreenState.EMPTY)
            } else {
                recognized.postValue(it)
                state.postValue(ScreenState.SUCCESS)
            }
        }, {
            state.postValue(ScreenState.ERROR)
        })
    }
}