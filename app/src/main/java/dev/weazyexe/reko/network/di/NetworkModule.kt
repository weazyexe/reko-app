package dev.weazyexe.reko.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.weazyexe.reko.network.SkyBiometryApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit =
        Retrofit.Builder()
            .build()

    @Provides
    @Singleton
    fun provideSkyBiometryApi(retrofit: Retrofit): SkyBiometryApi =
        retrofit.create(SkyBiometryApi::class.java)
}