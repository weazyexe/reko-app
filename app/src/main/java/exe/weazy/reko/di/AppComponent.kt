package exe.weazy.reko.di

import dagger.Component
import exe.weazy.reko.recognizer.SkyBiometryRecognizer
import exe.weazy.reko.ui.image.ImageViewModel
import exe.weazy.reko.ui.login.LoginViewModel
import exe.weazy.reko.ui.main.feed.FeedViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, RecognizerModule::class])
interface AppComponent {
    fun inject(feedViewModel: FeedViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(skyBiometryRecognizer: SkyBiometryRecognizer)
    fun inject(imageViewModel: ImageViewModel)
}