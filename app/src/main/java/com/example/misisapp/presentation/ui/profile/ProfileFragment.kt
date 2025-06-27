package com.example.misisapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.misisapp.databinding.FragmentProfileBinding
import com.example.misisapp.domain.model.User
import com.example.misisapp.domain.model.UserRole
import com.example.misisapp.presentation.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.state.collect { state ->
                    when (state) {
                        is ProfileState.Error -> {
                            showProfileFromCache()
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                        is ProfileState.Loading -> {
                            binding.skeletonLayout.showSkeleton()
                        }
                        is ProfileState.Success -> {
                            binding.skeletonLayout.showOriginal()
                            showProfile(state.user)
                        }
                    }
                }
            }
        }
    }

    private fun showProfileFromCache() {
        profileViewModel.getCachedProfile().observe(viewLifecycleOwner) { cached ->
            cached?.let { showProfile(it) }
        }
    }

    private fun showProfile(user: User) {
        binding.courseGroupInfo.text = when(user.role) {
            UserRole.STUDENT -> "СТУДЕНТ"
            UserRole.TEACHER -> "ПРЕПОДАВАТЕЛЬ"
            UserRole.ADMIN -> "АДМИНИСТРАТОР"
        }
        binding.userName.text = user.fullName
        binding.copyEmailButton.text = user.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}