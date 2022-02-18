package dev.weazyexe.reko.data.network

import dev.weazyexe.reko.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor for putting auth params to request
 */
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url()

        val url: HttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.APPLICATION_KEY)
            .addQueryParameter("api_secret", BuildConfig.APPLICATION_SECRET_KEY)
            .build()

        val requestBuilder = original.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }
}