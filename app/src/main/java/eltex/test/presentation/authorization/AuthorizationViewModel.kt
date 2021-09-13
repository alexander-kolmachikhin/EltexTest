package eltex.test.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eltex.test.domain.authorization.AuthorizationInteractor
import eltex.test.domain.authorization.AuthorizationResult
import eltex.test.presentation.user.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class AuthorizationViewModel(private val authorizationInteractor: AuthorizationInteractor) : ViewModel() {

    val usernameStateFlow = MutableStateFlow("")
    val passwordStateFlow = MutableStateFlow("")
    val authorizeResultStateFlow = MutableStateFlow<AuthorizationResult?>(null)
    val messageStateFlow = MutableStateFlow<Message?>(null)

    fun authorize() = viewModelScope.launch {
        try {
            val result = authorizationInteractor.authorize(
                usernameStateFlow.value,
                passwordStateFlow.value
            )

            authorizeResultStateFlow.value = result

            if (result is AuthorizationResult.Failure) {
                messageStateFlow.value = Message(result.body.errorDescription)
            }
        } catch (exception: UnknownHostException) {
            messageStateFlow.value = Message("No internet connection.")
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            messageStateFlow.value = Message(throwable.localizedMessage ?: "Error")
        }
    }

    class Message(
        val text: String
    )
}