package dev.weazyexe.reko.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.weazyexe.reko.data.network.AuthInterceptor
import dev.weazyexe.reko.data.network.SkyBiometryApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.skybiometry.com/fc/")
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideSkyBiometryApi(retrofit: Retrofit): SkyBiometryApi =
        retrofit.create(SkyBiometryApi::class.java)
}