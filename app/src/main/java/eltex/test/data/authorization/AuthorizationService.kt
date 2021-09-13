package eltex.test.data.authorization

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationService {

    @POST("api/v1/oauth/token")
    suspend fun authorize(
        @Header("Authorization") encodedCredentials: String,
        @Query("grant_type") grantType: String,
        @Query("username") username: String,
        @Query("password") password: String,
    ): Response<AuthorizationResponseBody>

}