package exe.weazy.reko.data.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface NetworkService {

    @Multipart
    @POST("faces/detect")
    fun recognize(@Part urls: MultipartBody.Part, @Query("attributes") attr: String = "all"): Observable<RecognizedResponse>
}