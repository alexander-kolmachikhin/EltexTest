package eltex.test

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.GsonBuilder
import eltex.test.data.authorization.AuthorizationService
import eltex.test.data.user.UserService
import eltex.test.domain.authorization.AuthorizationInteractor
import eltex.test.domain.user.UserInteractor
import eltex.test.presentation.authorization.AuthorizationViewModel
import eltex.test.presentation.main.MainViewModel
import eltex.test.presentation.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(
                module {
                    data()
                    domain()
                    presentation()
                }
            )
        }
    }

    private fun Module.presentation() {
        viewModel {
            MainViewModel(
                authorizationInteractor = get()
            )
        }
        viewModel {
            AuthorizationViewModel(
                authorizationInteractor = get()
            )
        }
        viewModel {
            UserViewModel(
                userInteractor = get(),
                authorizationInteractor = get()
            )
        }
    }

    private fun Module.domain() {
        single {
            UserInteractor(
                gson = get(),
                userService = get(),
                authorizationInteractor = get(),
                dataStore = PreferenceDataStoreFactory.create(
                    corruptionHandler = null,
                    migrations = emptyList(),
                    scope = CoroutineScope(Dispatchers.Default)
                ) { preferencesDataStoreFile("UserInteractor") }
            )
        }
        single {
            AuthorizationInteractor(
                gson = get(),
                authorizationService = get(),
                dataStore = PreferenceDataStoreFactory.create(
                    corruptionHandler = null,
                    migrations = emptyList(),
                    scope = CoroutineScope(Dispatchers.Default)
                ) { preferencesDataStoreFile("AuthorizationInteractor") }
            )
        }
    }

    private fun Module.data() {
        single { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }
        single { get<Retrofit>().create(AuthorizationService::class.java) }
        single { get<Retrofit>().create(UserService::class.java) }
        single {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(get()))
                .client(
                    OkHttpClient.Builder().addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    ).build()
                )
                .baseUrl("http://smart.eltex-co.ru:8271")
                .build()
        }
    }
}