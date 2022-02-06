package dev.weazyexe.reko.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.weazyexe.reko.data.network.SkyBiometryApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .build()

    @Provides
    @Singleton
    fun provideSkyBiometryApi(retrofit: Retrofit): SkyBiometryApi =
        retrofit.create(SkyBiometryApi::class.java)
}