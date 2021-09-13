package eltex.test.domain.authorization

import eltex.test.data.authorization.AuthorizationErrorBody
import eltex.test.data.authorization.AuthorizationResponseBody

sealed class AuthorizationResult {
    class Success : AuthorizationResult()
    class Failure(val body: AuthorizationErrorBody) : AuthorizationResult()
}