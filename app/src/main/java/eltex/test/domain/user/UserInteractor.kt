package eltex.test.domain.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import eltex.test.data.user.GetUserErrorBody
import eltex.test.data.user.GetUserResponseBody
import eltex.test.data.user.UserService
import eltex.test.domain.authorization.AuthorizationInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class UserInteractor(
    private val gson: Gson,
    private val userService: UserService,
    private val authorizationInteractor: AuthorizationInteractor,
    private val dataStore: DataStore<Preferences>
) {

    val userFlow = dataStore.data.map { it[PreferencesKey.USER_JSON]?.fromJsonToUserData() }

    suspend fun refreshUserData(): RefreshUserDataResult {
        val authorizationData = authorizationInteractor.authorizationDataFlow.first()
            ?: return RefreshUserDataResult.NotAuthorized()

        val response = userService.getUser("Bearer ${authorizationData.accessToken}")

        return response.body()?.let { body ->
            dataStore.edit { it[PreferencesKey.USER_JSON] = body.toUserData().toJson() }
            RefreshUserDataResult.Success()
        } ?: run {
            authorizationInteractor.logout()
            RefreshUserDataResult.Failure(
                response.errorBody()!!.string().trim().fromJsonToGetUserErrorBody()
            )
        }
    }

    private fun String.fromJsonToGetUserErrorBody() = gson.fromJson(this, GetUserErrorBody::class.java)

    private fun GetUserResponseBody.toUserData() = UserData(
        id, roleId, username, email, permissions
    )

    private fun UserData.toJson() = gson.toJson(this)

    private fun String.fromJsonToUserData() = gson.fromJson(this, UserData::class.java)

    private object PreferencesKey {
        val USER_JSON = stringPreferencesKey("userJson")
    }
}