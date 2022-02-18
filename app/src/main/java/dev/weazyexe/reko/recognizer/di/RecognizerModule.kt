package dev.weazyexe.reko.recognizer.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dev.weazyexe.reko.data.network.SkyBiometryApi
import dev.weazyexe.reko.recognizer.Recognizer
import dev.weazyexe.reko.recognizer.SkyBiometryRecognizer

@Module
@InstallIn(ViewModelComponent::class)
class RecognizerModule {

    @Provides
    @ViewModelScoped
    fun provideRecognizer(
        skyBiometryApi: SkyBiometryApi,
        @ApplicationContext context: Context
    ): Recognizer = SkyBiometryRecognizer(skyBiometryApi, context)
}