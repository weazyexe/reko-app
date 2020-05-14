package exe.weazy.reko.di

import android.content.Context
import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.ApiKeyRepository
import exe.weazy.reko.data.AuthRepository
import exe.weazy.reko.data.RecognizedRepository
import exe.weazy.reko.data.SettingsRepository
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideRecognizedRepository() = RecognizedRepository()

    @Provides
    fun provideApiKeyRepository() = ApiKeyRepository(context)

    @Provides
    fun provideAuthRepository() = AuthRepository()

    @Provides
    fun provideSettingsRepository() = SettingsRepository(context)
}