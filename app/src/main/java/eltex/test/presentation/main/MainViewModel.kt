package eltex.test.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eltex.test.domain.authorization.AuthorizationInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MainViewModel(authorizationInteractor: AuthorizationInteractor) : ViewModel() {

    val authorizationFlow = authorizationInteractor.authorizationDataFlow.map {
        when (it) {
            null -> AuthorizationState.NotAuthorized()
            else -> AuthorizationState.Authorized()
        }
    }

    sealed class AuthorizationState {
        class NotAuthorized : AuthorizationState()
        class Authorized : AuthorizationState()
    }
}