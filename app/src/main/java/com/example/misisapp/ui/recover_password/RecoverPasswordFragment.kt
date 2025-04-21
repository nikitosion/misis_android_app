package com.example.misisapp.ui.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentRecoverPasswordBinding

class RecoverPasswordFragment : Fragment() {
    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

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


        val usernameEditText = binding.username
        usernameEditText.isEnabled = true
        val recoverButton = binding.recoverButton
        val backButton = binding.back
        val loadingProgressBar = binding.loading

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_recoverPasswordFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}