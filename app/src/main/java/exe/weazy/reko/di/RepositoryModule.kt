package exe.weazy.reko.di

import android.content.Context
import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.MemesRepository
import exe.weazy.reko.data.storage.UserStorage
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideMemesRepository() = MemesRepository()

    @Provides
    @Singleton
    fun provideUserStorage() = UserStorage(context)
}