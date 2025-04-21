package com.example.misisapp.ui.organizations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.misisapp.databinding.FragmentOrganizationsBinding

class OrganizationsFragment : Fragment() {

    private var _binding: FragmentOrganizationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val organizationsViewModel =
            ViewModelProvider(this).get(OrganizationsViewModel::class.java)*/

        _binding = FragmentOrganizationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textOrganizations
        organizationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}