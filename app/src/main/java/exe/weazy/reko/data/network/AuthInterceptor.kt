package exe.weazy.reko.data.network

import exe.weazy.reko.data.ApiKeyRepository
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val apiKeyRepository: ApiKeyRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url()

        val url: HttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", apiKeyRepository.getApplicationKey())
            .addQueryParameter("api_secret", apiKeyRepository.getApplicationSecretKey())
            .build()

        val requestBuilder = original.newBuilder().url(url)

        return chain.proceed(requestBuilder.build())
    }
}