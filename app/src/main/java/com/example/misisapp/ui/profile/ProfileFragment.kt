package com.example.misisapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.misisapp.R
import com.example.misisapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val organizationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        organizationsViewModel.user.observe(viewLifecycleOwner) { user ->
            view?.findViewById<TextView>(R.id.email)?.text = user.email
            view?.findViewById<TextView>(R.id.user_name)?.text = user.fullName
            view?.findViewById<TextView>(R.id.course_group_info)?.text = user.groupName
        }

        organizationsViewModel.loadProfile()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}