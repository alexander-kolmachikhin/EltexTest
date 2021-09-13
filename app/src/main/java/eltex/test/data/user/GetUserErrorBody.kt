package eltex.test.data.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetUserErrorBody(
    @Expose
    @SerializedName("error")
    val error: String,

    @Expose
    @SerializedName("error_description")
    val errorDescription: String
)