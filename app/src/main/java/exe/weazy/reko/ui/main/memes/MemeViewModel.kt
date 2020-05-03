package exe.weazy.reko.ui.main.memes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.reko.data.MemesRepository
import exe.weazy.reko.data.storage.UserStorage
import exe.weazy.reko.di.App
import exe.weazy.reko.model.Meme
import exe.weazy.reko.model.UserInfo
import exe.weazy.reko.state.ScreenState
import exe.weazy.reko.util.extensions.subscribe
import javax.inject.Inject

class MemeViewModel: ViewModel() {

    @Inject
    lateinit var memesRepository: MemesRepository

    @Inject
    lateinit var userStorage: UserStorage

    var meme = MutableLiveData<Meme>()

    var userInfo = MutableLiveData<UserInfo>()

    var state = MutableLiveData<ScreenState>(ScreenState.DEFAULT)

    init {
        App.getComponent().injectMemeViewModel(this)
    }

    fun getMeme(id: Long) {
        state.postValue(ScreenState.LOADING)
        subscribe(memesRepository.getById(id), {
            meme.postValue(it)
            state.postValue(ScreenState.SUCCESS)
        }, {
            state.postValue(ScreenState.ERROR)
        })
    }

    fun getUserInfo() {
        userInfo.postValue(userStorage.getUserInfo())
    }

    fun likeMeme() {
        val meme = meme.value
        if (meme != null) {
            memesRepository.like(meme)
        }
    }
}