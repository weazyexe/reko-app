package exe.weazy.reko.di

import dagger.Component
import exe.weazy.reko.ui.main.feed.FeedViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(feedViewModel: FeedViewModel)
}