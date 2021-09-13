package eltex.test.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import eltex.test.R
import eltex.test.core.dropFirstIf
import eltex.test.databinding.MainActivityBinding
import eltex.test.presentation.authorization.AuthorizationFragment
import eltex.test.presentation.user.UserFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authorizationFlow.dropFirstIf { savedInstanceState != null }.onEach {
            Log.d("test123", "onEach: $it")
            when (it) {
                is MainViewModel.AuthorizationState.NotAuthorized -> {
                    supportFragmentManager.commit {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        replace(
                            R.id.fragmentContainerView,
                            AuthorizationFragment::class.java,
                            null
                        )
                    }
                }
                is MainViewModel.AuthorizationState.Authorized -> {
                    supportFragmentManager.commit {
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        replace(
                            R.id.fragmentContainerView,
                            UserFragment::class.java,
                            null
                        )
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }
}