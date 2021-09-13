package eltex.test.domain.user

import eltex.test.data.user.GetUserErrorBody

sealed class RefreshUserDataResult {
    class NotAuthorized : RefreshUserDataResult()
    class Success : RefreshUserDataResult()
    class Failure(val body: GetUserErrorBody) : RefreshUserDataResult()
}