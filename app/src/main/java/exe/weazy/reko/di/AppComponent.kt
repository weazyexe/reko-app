package exe.weazy.reko.di

import dagger.Component
import exe.weazy.reko.recognizer.SkyBiometryRecognizer
import exe.weazy.reko.ui.auth.AuthViewModel
import exe.weazy.reko.ui.image.ImageViewModel
import exe.weazy.reko.ui.main.MainViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, RecognizerModule::class])
interface AppComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(authViewModel: AuthViewModel)
    fun inject(skyBiometryRecognizer: SkyBiometryRecognizer)
    fun inject(imageViewModel: ImageViewModel)
}