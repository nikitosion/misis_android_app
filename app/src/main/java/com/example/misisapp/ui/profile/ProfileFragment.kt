package com.example.misisapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentProfileBinding
import com.example.misisapp.ui.login.data.Result
import com.faltenreich.skeletonlayout.Skeleton

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var skeleton: Skeleton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val emailField = binding.email
        val userNameField = binding.userName
        val courseGroupInfoField = binding.courseGroupInfo
        val skeleton = binding.skeletonLayout

        skeleton.showSkeleton()

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            skeleton.showOriginal()
            emailField.text = user.email
            userNameField.text = user.fullName
            courseGroupInfoField.text = user.groupName
        }

        profileViewModel.loadProfile()

        profileViewModel.getInfoResult.observe(viewLifecycleOwner) { getInfoResult ->
           if (getInfoResult is Result.Error) {
               profileViewModel.clearSavedUserToken()
               findNavController().navigate(R.id.action_navigation_profile_to_loginFragment)
           }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}