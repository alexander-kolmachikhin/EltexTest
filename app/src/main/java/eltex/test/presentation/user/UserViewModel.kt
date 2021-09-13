package eltex.test.presentation.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eltex.test.domain.authorization.AuthorizationInteractor
import eltex.test.domain.user.RefreshUserDataResult
import eltex.test.domain.user.UserData
import eltex.test.domain.user.UserInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class UserViewModel(
    private val userInteractor: UserInteractor,
    private val authorizationInteractor: AuthorizationInteractor
) : ViewModel() {

    val userFlow = userInteractor.userFlow.map {
        if (it == null) {
            UserState.Loading()
        } else {
            UserState.Loaded(it)
        }
    }

    val refreshStateFlow = MutableStateFlow<RefreshUserState>(RefreshUserState.Loading())

    init {
        refreshUser()
    }

    fun refreshUser() = viewModelScope.launch {
        refreshStateFlow.value = RefreshUserState.Loading()
        try {
            when (val result = userInteractor.refreshUserData()) {
                is RefreshUserDataResult.Failure -> {
                    refreshStateFlow.value = RefreshUserState.Failure(result.body.errorDescription)
                }
                is RefreshUserDataResult.Success -> {
                    refreshStateFlow.value = RefreshUserState.Loaded()
                }
                is RefreshUserDataResult.NotAuthorized -> {
                    refreshStateFlow.value = RefreshUserState.Failure("Not authorized")
                }
            }
        } catch (exception: UnknownHostException) {
            refreshStateFlow.value = RefreshUserState.Failure("No internet connection.")
        } catch (throwable: Throwable) {
            refreshStateFlow.value = RefreshUserState.Failure(throwable.localizedMessage ?: "Error")
        }
    }

    fun logout() = viewModelScope.launch {
        authorizationInteractor.logout()
    }

    sealed class RefreshUserState {
        class Loading : RefreshUserState()
        class Failure(val error: String) : RefreshUserState()
        class Loaded : RefreshUserState()
    }

    sealed class UserState {
        class Loading : UserState()
        class Loaded(val userData: UserData) : UserState()
    }
}