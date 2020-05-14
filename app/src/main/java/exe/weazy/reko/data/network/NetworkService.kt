package exe.weazy.reko.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface NetworkService {

    @Multipart
    @POST("faces/detect")
    fun recognize(@Part urls: MultipartBody.Part, @Query("attributes") attr: String = "all"): Observable<RecognizedResponse>
}