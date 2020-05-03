package exe.weazy.reko.data.network

import exe.weazy.reko.data.network.requests.LoginPasswordRequest
import exe.weazy.reko.data.network.responses.AuthResponse
import exe.weazy.reko.data.network.responses.MemesResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {

    @POST("/auth/login")
    fun signIn(@Body loginPasswordRequest: LoginPasswordRequest) : Observable<AuthResponse>

    @GET("/memes")
    fun fetchMemes() : Observable<List<MemesResponse>>
}