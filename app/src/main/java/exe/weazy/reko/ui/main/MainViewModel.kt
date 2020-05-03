package exe.weazy.reko.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.MemesRepository
import exe.weazy.reko.data.storage.UserStorage
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Meme
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.subscribe
import javax.inject.Inject

class MainViewModel: ViewModel() {

    @Inject
    lateinit var memesRepository: MemesRepository

    @Inject
    lateinit var userStorage: UserStorage

    val memes = MutableLiveData<List<Meme>>(listOf())
    val memesState = MutableLiveData<ScreenState>(ScreenState.DEFAULT)

    init {
        App.getComponent().injectMainViewModel(this)
    }

    fun getUserToken(): String {
        return userStorage.getAccessToken()
    }

    fun fetchMemes() {
        memesState.postValue(ScreenState.LOADING)

        subscribe(memesRepository.fetch(), { memes ->
            this.memes.postValue(memes)
            memesRepository.save(memes)

            if (memes.isEmpty()) {
                memesState.postValue(ScreenState.EMPTY)
            } else {
                memesState.postValue(ScreenState.SUCCESS)
            }
        }, {
            memesState.postValue(ScreenState.ERROR)
        })
    }

    fun refreshMemes() {
        if (memesState.value == ScreenState.SUCCESS || memesState.value == ScreenState.EMPTY) {
            memes.postValue(memesRepository.memes)
        }
    }

    fun likeMeme(meme: Meme) {
        memesRepository.like(meme)
        this.memes.postValue(memesRepository.memes)
    }
}