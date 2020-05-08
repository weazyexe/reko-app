package exe.weazy.reko.di

import android.content.Context
import dagger.Module
import dagger.Provides
import exe.weazy.reko.data.network.AuthInterceptor
import exe.weazy.reko.data.network.NetworkService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://demo2407529.mockable.io")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    fun provideNetworkService(retrofit: Retrofit) : NetworkService
            = retrofit.create(NetworkService::class.java)
}