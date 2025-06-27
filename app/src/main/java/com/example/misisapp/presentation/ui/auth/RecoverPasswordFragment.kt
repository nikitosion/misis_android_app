package com.example.misisapp.presentation.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.misisapp.databinding.FragmentRecoverPasswordBinding
import com.example.misisapp.presentation.viewmodel.RecoverPasswordNavigationEvent
import com.example.misisapp.presentation.viewmodel.RecoverPasswordUiState
import com.example.misisapp.presentation.viewmodel.RecoverPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

    private val recoverPasswordViewModel: RecoverPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.apply {
            recoverButton.setOnClickListener {
                val email = emailToRecover.text.toString().trim()
                recoverPasswordViewModel.recoverPassword(email)
            }

            backButton.setOnClickListener {
                recoverPasswordViewModel.navigateBack()
            }

            emailToRecover.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    recoverPasswordViewModel.clearError()
                }
            })
        }
    }

    private fun observeViewModel() {
        recoverPasswordViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RecoverPasswordUiState.Loading -> {
                    showLoading(true)
                    hideMessages()
                    enableForm(false)
                }
                is RecoverPasswordUiState.ShowForm -> {
                    showLoading(false)
                    hideMessages()
                    enableForm(true)
                }
                is RecoverPasswordUiState.Success -> {
                    showLoading(false)
                    showSuccess(state.message)
                    enableForm(false)
                }
                is RecoverPasswordUiState.Error -> {
                    showLoading(false)
                    showError(state.message)
                    enableForm(true)
                }
            }
        }

        recoverPasswordViewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.let {
                when (it) {
                    RecoverPasswordNavigationEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                }
                recoverPasswordViewModel.clearNavigationEvent()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            recoverLayout.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    // TODO: implement showing and hiding errors
    private fun showError(message: String) {
        /*binding.tvError.apply {
            text = message
            visibility = View.VISIBLE
        }*/
    }

    private fun showSuccess(message: String) {
        /*binding.tvSuccess.apply {
            text = message
            visibility = View.VISIBLE
        }*/
    }

    private fun hideMessages() {
        /*binding.apply {
            tvError.visibility = View.GONE
            tvSuccess.visibility = View.GONE
        }*/
    }

    private fun enableForm(enabled: Boolean) {
        binding.apply {
            emailToRecover.isEnabled = enabled
            recoverButton.isEnabled = enabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}