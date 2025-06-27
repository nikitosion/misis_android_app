package com.example.misisapp.presentation.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentLoginBinding
import com.example.misisapp.presentation.viewmodel.LoginUiState
import com.example.misisapp.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    loginViewModel.uiState.collect { resource ->
                        when (resource) {
                            is LoginUiState.Idle -> {
                                showLoading(false)
                                enableForm(true)
                            }

                            is LoginUiState.Loading -> {
                                showLoading(true)
                            }

                            is LoginUiState.Success -> {
                                navigateToSchedule()
                                enableForm(false)
                                showLoading(false)
                            }

                            is LoginUiState.Error -> {
                                showLoading(false)
                                showError(resource.message)
                                enableForm(true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToSchedule() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_schedule)
    }

    private fun navigateToRecoverPassword() {
        findNavController().navigate(R.id.action_navigation_login_to_navigation_recover_password)
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressCirlce.visibility = if (show) View.VISIBLE else View.GONE
            authLayout.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    // TODO: implement showing and hiding error
    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun enableForm(enabled: Boolean) {
        binding.apply {
            email.isEnabled = enabled
            password.isEnabled = enabled
            loginButton.isEnabled = enabled
            recoverPassword.isEnabled = enabled
        }
    }

    private fun setupViews() {
        binding.apply {
            loginButton.setOnClickListener {
                val username = email.text.toString().trim()
                val password = password.text.toString().trim()
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                loginViewModel.login(username, password)
            }

            recoverPassword.setOnClickListener {
                navigateToRecoverPassword()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}