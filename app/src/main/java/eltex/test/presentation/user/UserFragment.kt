package eltex.test.presentation.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import eltex.test.R
import eltex.test.core.dropFirstIf
import eltex.test.databinding.UserFragmentBinding
import eltex.test.domain.user.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : Fragment(R.layout.user_fragment) {

    private val viewModel: UserViewModel by viewModel()
    private val binding: UserFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        permissionsRecyclerView.adapter = UserPermissionsAdapter()

        viewModel.userFlow.onEach {
            when (it) {
                is UserViewModel.UserState.Loaded -> {
                    loadedGroup.isVisible = true
                    loadingGroup.isVisible = false
                    showUserData(it.userData)
                }
                is UserViewModel.UserState.Loading -> {
                    loadedGroup.isVisible = false
                    loadingGroup.isVisible = true
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.refreshStateFlow.onEach {
            root.isRefreshing = it is UserViewModel.RefreshUserState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.refreshStateFlow.dropFirstIf { savedInstanceState != null }.onEach {
            if (it is UserViewModel.RefreshUserState.Failure) {
                Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        logoutButton.setOnClickListener {
            logoutButton.isEnabled = false
            viewModel.logout()
        }

        root.setOnRefreshListener {
            viewModel.refreshUser()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUserData(userData: UserData) = with(binding) {
        usernameTextView.text = userData.username
        emailTextView.text = "E-mail: ${userData.email}"
        roleIdTextView.text = "Role id: ${userData.roleId}"
        (permissionsRecyclerView.adapter as UserPermissionsAdapter).submitList(
            userData.permissions.mapIndexed { index, s ->
                UserPermissionsAdapter.Item(index, s)
            }
        )
    }

}