package exe.weazy.reko.di

import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.RecognizedRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRecognizedRepository() = RecognizedRepository()
}