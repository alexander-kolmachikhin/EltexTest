package eltex.test.presentation.authorization

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import eltex.test.R
import eltex.test.core.dropFirstIf
import eltex.test.core.setTextIfNotEquals
import eltex.test.databinding.AuthorizationFragmentBinding
import eltex.test.domain.authorization.AuthorizationResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment(R.layout.authorization_fragment) {

    private val viewModel: AuthorizationViewModel by viewModel()
    private val binding: AuthorizationFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.messageStateFlow.dropFirstIf { savedInstanceState != null }.onEach {
            if (it != null) {
                Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.authorizeResultStateFlow.dropFirstIf { savedInstanceState != null }.onEach {

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.usernameStateFlow
            .onEach(usernameTextInputEditText::setTextIfNotEquals)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.passwordStateFlow
            .onEach(passwordTextInputEditText::setTextIfNotEquals)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        usernameTextInputEditText.doAfterTextChanged {
            viewModel.usernameStateFlow.value = it?.toString() ?: ""
        }

        passwordTextInputEditText.doAfterTextChanged {
            viewModel.passwordStateFlow.value = it?.toString() ?: ""
        }

        authorizeMaterialButton.setOnClickListener {
            viewModel.authorize()
        }
    }
}