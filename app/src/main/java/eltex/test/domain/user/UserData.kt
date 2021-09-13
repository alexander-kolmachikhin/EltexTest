package eltex.test.domain.user

import com.google.gson.annotations.Expose

data class UserData(
    @Expose
    val id: String,

    @Expose
    val roleId: String,

    @Expose
    val username: String,

    @Expose
    val email: String?,

    @Expose
    val permissions: List<String>
)