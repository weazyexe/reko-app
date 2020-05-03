package exe.weazy.reko.di

import dagger.Component
import exe.weazy.reko.data.AuthRepository
import exe.weazy.reko.data.MemesRepository
import exe.weazy.reko.ui.login.LoginViewModel
import exe.weazy.reko.ui.main.MainViewModel
import exe.weazy.reko.ui.main.create.CreateMemeViewModel
import exe.weazy.reko.ui.main.memes.MemeViewModel
import exe.weazy.reko.ui.main.profile.ProfileViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {
    fun injectAuthRepository(repository: AuthRepository)
    fun injectMemesRepository(repository: MemesRepository)
    fun injectMainViewModel(mainViewModel: MainViewModel)
    fun injectMemeViewModel(memeViewModel: MemeViewModel)
    fun injectCreateMemeViewModel(createMemeViewModel: CreateMemeViewModel)
    fun injectLoginViewModel(loginViewModel: LoginViewModel)
    fun injectProfileViewModel(profileViewModel: ProfileViewModel)
}