package dev.weazyexe.reko.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.weazyexe.core.utils.providers.StringsProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideStringsProvider(@ApplicationContext context: Context): StringsProvider =
        StringsProvider(context)
}