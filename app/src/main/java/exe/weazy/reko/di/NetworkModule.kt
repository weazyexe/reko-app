package exe.weazy.reko.di

import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.ApiKeyRepository
import exe.weazy.reko.data.network.AuthInterceptor
import exe.weazy.reko.data.network.NetworkService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(apiKeyRepository: ApiKeyRepository): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(apiKeyRepository))
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.skybiometry.com/fc/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    fun provideNetworkService(retrofit: Retrofit) : NetworkService
            = retrofit.create(NetworkService::class.java)
}