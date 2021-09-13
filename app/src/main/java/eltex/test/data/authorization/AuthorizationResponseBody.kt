package eltex.test.data.authorization

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorizationResponseBody(
    @Expose
    @SerializedName("access_token")
    val accessToken: String,

    @Expose
    @SerializedName("access_type")
    val tokenType: String,

    @Expose
    @SerializedName("refresh_token")
    val refreshToken: String,

    @Expose
    @SerializedName("expires_in")
    val expiresIn: Long,

    @Expose
    @SerializedName("scope")
    val scope: String
)