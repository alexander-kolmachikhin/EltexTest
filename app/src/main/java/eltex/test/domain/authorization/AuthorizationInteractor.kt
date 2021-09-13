package eltex.test.domain.authorization

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import eltex.test.data.authorization.AuthorizationErrorBody
import eltex.test.data.authorization.AuthorizationService
import kotlinx.coroutines.flow.map

@Suppress("BlockingMethodInNonBlockingContext")
class AuthorizationInteractor(
    private val gson: Gson,
    private val authorizationService: AuthorizationService,
    private val dataStore: DataStore<Preferences>
) {

    val authorizationDataFlow = dataStore.data.map {
        it[PreferencesKey.ACCESS_TOKEN]?.let(::AuthorizationData)
    }

    suspend fun logout() = dataStore.edit { it.remove(PreferencesKey.ACCESS_TOKEN) }

    suspend fun authorize(username: String, password: String): AuthorizationResult {
        val response = authorizationService.authorize(
            "Basic ${Base64.encodeToString("android-client:password".toByteArray(), Base64.NO_WRAP)}",
            "password",
            username,
            password
        )

        return response.body()?.let { body ->
            dataStore.edit { it[PreferencesKey.ACCESS_TOKEN] = body.accessToken }
            AuthorizationResult.Success()
        } ?: AuthorizationResult.Failure(
            response.errorBody()!!.string().trim().fromJsonToAuthorizationErrorBody()
        )
    }

    private fun String.fromJsonToAuthorizationErrorBody() = gson.fromJson(this, AuthorizationErrorBody::class.java)

    private object PreferencesKey {
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    }
}