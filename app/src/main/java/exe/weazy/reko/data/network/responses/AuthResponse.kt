package exe.weazy.reko.data.network.responses

data class AuthResponse(
    val accessToken: String,
    val userInfo: UserInfoResponse?
)