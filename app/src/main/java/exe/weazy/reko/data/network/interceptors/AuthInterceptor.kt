package exe.weazy.reko.data.network.interceptors

import exe.weazy.reko.data.storage.UserStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val storage: UserStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val headers = originalRequest.headers().newBuilder()
            .add("Authorization", storage.getAccessToken())
            .build()

        val newRequest = originalRequest.newBuilder()
            .headers(headers)
            .build()

        return chain.proceed(newRequest)
    }
}