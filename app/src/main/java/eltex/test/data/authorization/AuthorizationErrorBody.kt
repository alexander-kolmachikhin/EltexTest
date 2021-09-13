package eltex.test.data.authorization

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorizationErrorBody(
    @Expose
    @SerializedName("error")
    val error: String,

    @Expose
    @SerializedName("error_description")
    val errorDescription: String
)