package dev.weazyexe.reko.data.network

import dev.weazyexe.reko.data.network.response.RecognizedImageEntity
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Retrofit API for SkyBiometry service
 */
interface SkyBiometryApi {

    /**
     * Recognizes facial emotions on the image from [urls]
     */
    @Multipart
    @POST("faces/detect")
    suspend fun recognize(
        @Part urls: MultipartBody.Part,
        @Query("attributes") attr: String = "all"
    ): RecognizedImageEntity
}