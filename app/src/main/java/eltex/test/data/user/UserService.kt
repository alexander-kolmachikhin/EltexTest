package eltex.test.data.user

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    @GET("api/v1/user")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): Response<GetUserResponseBody>


}