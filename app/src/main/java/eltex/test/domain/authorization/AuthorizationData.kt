package eltex.test.domain.authorization

import com.google.gson.annotations.Expose

data class AuthorizationData (
    @Expose
    val accessToken: String
)